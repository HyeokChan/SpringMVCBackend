
# Springboot MVC

## 개발 / 학습 환경
- Project : Gradle Project
- Spring Boot : 2.3
- Language : Java
- Pajaging : Jar
- Java : 11

## 서블릿
### HttpServletRequest 개요
#### HttpServletRequest 역할
서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 HTTP 요청 메시지를 파싱한다. 그리고 그 결과를 HttpServletRequest 객체에 담아서 제공한다.
#### HTTP 요청 메시지
	POST /save HTTP/1.1
	Host: localhost:8080
	Content-Type: application/x-www-form-urlencoded
	username=kim&age=20
### HTTP 요청 데이터 - 개요
HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 방법
1. GET - 쿼리 파라미터
	- /url?username=hello&age=20
	- 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
	- 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
2. HTML Form
	- content-type: application/x-www-form-urlencoded
	- 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20
	- 예) 회원 가입, 상품 주문, HTML Form 사용
3. HTTP message body에 데이터를 직접 담아서 요청
	- HTTP API에서 주로 사용, JSON, XML, TEXT
	- 데이터 형식은 주로 JSON 사용
	- POST, PUT, PATCH
### HTTP 요청 데이터 - GET 쿼리 파라미터
#### 쿼리 파라미터 조회 메서드
	String username = request.getParameter("username"); //단일 파라미터 조회
	Enumeration<String> parameterNames = request.getParameterNames(); //파라미터 이름들 모두 조회
	Map<String, String[]> parameterMap = request.getParameterMap(); //파라미터를 Map 으로 조회
	String[] usernames = request.getParameterValues("username"); //복수 파라미터 조회
### HTTP 요청 데이터 - POST HTML Form
#### 특징
- content-type: application/x-www-form-urlencoded
- 메시지 바디에 쿼리 파리미터 형식으로 데이터를 전달한다. username=hello&age=20
- content-type: application/x-www-form-urlencoded
- message body: username=hello&age=20
### HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트
- HTTP message body에 데이터를 직접 담아서 요청
- HTTP API에서 주로 사용, JSON, XML, TEXT
- 데이터 형식은 주로 JSON 사용
- POST, PUT, PATCH
### HTTP 요청 데이터 - API 메시지 바디 - JSON
- POST http://localhost:8080/request-body-json
- content-type: application/json
- message body: {"username": "hello", "age": 20}
- 결과: messageBody = {"username": "hello", "age": 20}
#### code
	HelloData helloData = objectMapper.readValue(messageBody,
	HelloData.class);
	System.out.println("helloData.username = " + helloData.getUsername());
	System.out.println("helloData.age = " + helloData.getAge());
### HttpServletResponse - 기본 사용법
	//[status-line]
	response.setStatus(HttpServletResponse.SC_OK); //200
	//[response-headers]
	response.setHeader("Content-Type", "text/plain;charset=utf-8");
	response.setHeader("Cache-Control", "no-cache, no-store, mustrevalidate");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("my-header","hello");
	response.setContentType("text/plain");
	response.setCharacterEncoding("utf-8");
	Cookie cookie = new Cookie("myCookie", "good");
	cookie.setMaxAge(600); //600초
	response.addCookie(cookie);
	response.sendRedirect("/basic/hello-form.html");
### HTTP 응답 데이터 - 단순 텍스트, HTML
	@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
	public class ResponseHtmlServlet extends HttpServlet {
		@Override
		protected void service(HttpServletRequest request, HttpServletResponse
		response) throws ServletException, IOException {
			//Content-Type: text/html;charset=utf-8
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			PrintWriter writer = response.getWriter();
			writer.println("<html>");
			writer.println("<body>");
			writer.println(" <div>안녕?</div>");
			writer.println("</body>");
			writer.println("</html>");
		}
	}
### HTTP 응답 데이터 - API JSON
	@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
	public class ResponseJsonServlet extends HttpServlet {
		private ObjectMapper objectMapper = new ObjectMapper();
		@Override
		protected void service(HttpServletRequest request, HttpServletResponse
		response) throws ServletException, IOException {
			//Content-Type: application/json
			response.setHeader("content-type", "application/json");
			response.setCharacterEncoding("utf-8");
			HelloData data = new HelloData();
			data.setUsername("kim");
			data.setAge(20);
			//{"username":"kim","age":20}
			String result = objectMapper.writeValueAsString(data);
			response.getWriter().write(result);
		}
	}
## 서블릿, JSP, MVC 패턴
### 서블릿으로 회원 관리 웹 애플리케이션 만들기
	PrintWriter w = response.getWriter();
	w.write("<!DOCTYPE html>\n" +
		"<html>\n" +
		"<head>\n" +
		" <meta charset=\"UTF-8\">\n" +
		" <title>Title</title>\n" +
		"</head>\n" +
		"<body>\n" +
		"<form action=\"/servlet/members/save\" method=\"post\">\n" +
		" username: <input type=\"text\" name=\"username\" />\n" +
		" age: <input type=\"text\" name=\"age\" />\n" +
		" <button type=\"submit\">전송</button>\n" +
		"</form>\n" +
		"</body>\n" +
		"</html>\n");
자바 코드로 HTML을 제공해야 하므로 쉽지 않은 작업이다.
로직 처리 후 응답 HTML도 자바코드로 처리한다.

		for (Member member : members) {
			w.write(" <tr>");
			w.write(" <td>" + member.getId() + "</td>");
			w.write(" <td>" + member.getUsername() + "</td>");
			w.write(" <td>" + member.getAge() + "</td>");
			w.write(" </tr>");
		}

#### 템플릿 엔진으로
서블릿과 자바 코드만으로 HTML을 만들어보았다. 서블릿 덕분에 동적으로 원하는 HTML을
마음껏 만들 수 있다. 정적인 HTML 문서라면 화면이 계속 달라지는 회원의 저장 결과라던가, 회원 목록 같은 동적인 HTML을 만드는 일은 불가능 할 것이다.
그런데, 코드에서 보듯이 이것은 매우 복잡하고 비효율 적이다. 자바 코드로 HTML을 만들어 내는 것 보다 차라리 HTML 문서에 동적으로 변경해야 하는 부분만 자바 코드를 넣을 수 있다면 더 편리할 것이다.
이것이 바로 템플릿 엔진이 나온 이유이다. 템플릿 엔진을 사용하면 HTML 문서에서 필요한 곳만 코드를 적용해서 동적으로 변경할 수 있다.
템플릿 엔진에는 JSP, Thymeleaf, Freemarker, Velocity등이 있다.

### JSP로 회원 관리 웹 애플리케이션 만들기
	JSP는 자바 코드를 그대로 다 사용할 수 있다.
	<%@ page import="hello.servlet.domain.member.MemberRepository" %>
	자바의 import 문과 같다.
	<% ~~ %>
	이 부분에는 자바 코드를 입력할 수 있다.
	<%= ~~ %>
	이 부분에는 자바 코드를 출력할 수 있다.
### 서블릿과 JSP의 한계
서블릿으로 개발할 때는 뷰(View)화면을 위한 HTML을 만드는 작업이 자바 코드에 섞여서 지저분하고
복잡했다.
JSP를 사용한 덕분에 뷰를 생성하는 HTML 작업을 깔끔하게 가져가고, 중간중간 동적으로 변경이 필요한
부분에만 자바 코드를 적용했다. 그런데 이렇게 해도 해결되지 않는 몇가지 고민이 남는다.
코드의 상위 절반은 회원을 저장하기 위한 비즈니스 로직이고, 나머지 하위 절반만
결과를 HTML로 보여주기 위한 뷰 영역이다. 
코드를 잘 보면, JAVA 코드, 데이터를 조회하는 리포지토리 등등 다양한 코드가 모두 JSP에 노출되어 있다.
JSP가 너무 많은 역할을 한다.
## MVC 패턴 - 개요
### 너무 많은 역할
하나의 서블릿이나 JSP만으로 비즈니스 로직과 뷰 렌더링까지 모두 처리하게 되면, 너무 많은 역할을
하게되고, 결과적으로 유지보수가 어려워진다. 비즈니스 로직을 호출하는 부분에 변경이 발생해도 해당
코드를 손대야 하고, UI를 변경할 일이 있어도 비즈니스 로직이 함께 있는 해당 파일을 수정해야 한다.
### 변경의 라이프 사이클
둘 사이에 변경의 라이프 사이클이 다르다.
UI 를 일부 수정하는 일과 비즈니스 로직을 수정하는 일은 각각 다르게 발생할 가능성이 매우 높고 대부분
서로에게 영향을 주지 않는다. 이렇게 변경의 라이프 사이클이 다른 부분을 하나의 코드로 관리하는 것은
유지보수하기 좋지 않다.
### 기능 특화
JSP 같은 뷰 템플릿은 화면을 렌더링 하는데 최적화 되어 있기 때문에 이 부분의 업무만 담당하는 것이
가장 효과적이다.
### Model View Controller
MVC 패턴은 지금까지 학습한 것 처럼 하나의 서블릿이나, JSP로 처리하던 것을 컨트롤러(Controller)와
뷰(View)라는 영역으로 서로 역할을 나눈 것이다.
	- 컨트롤러: HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 그리고 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.
	- 모델: 뷰에 출력할 데이터를 담아둔다. 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는
비즈니스 로직이나 데이터 접근을 몰라도 되고, 화면을 렌더링 하는 일에 집중할 수 있다.
	- 뷰: 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 여기서는 HTML을 생성하는 부분을 말한다.
![mvc2](https://user-images.githubusercontent.com/48059565/136823534-4f516b20-2682-411a-9f55-8166ff490e46.jpg)
## MVC 패턴 - 적용

서블릿을 컨트롤러로 사용하고, JSP를 뷰로 사용해서 MVC 패턴을 적용해보자.
Model은 HttpServletRequest 객체를 사용한다. request는 내부에 데이터 저장소를 가지고 있는데, request.setAttribute() , request.getAttribute() 를 사용하면 데이터를 보관하고, 조회할 수 있다.

- dispatcher.forward() : 다른 서블릿이나 JSP로 이동할 수 있는 기능이다. 서버 내부에서 다시 호출이
발생한다.
- /WEB-INF : 이 경로안에 JSP가 있으면 외부에서 직접 JSP를 호출할 수 없다. 우리가 기대하는 것은 항상 컨트롤러를 통해서 JSP를 호출하는 것이다.
- redirect vs forward :
 리다이렉트는 실제 클라이언트(웹 브라우저)에 응답이 나갔다가, 클라이언트가 redirect 경로로 다시
요청한다. 따라서 클라이언트가 인지할 수 있고, URL 경로도 실제로 변경된다. 반면에 포워드는 서버
내부에서 일어나는 호출이기 때문에 클라이언트가 전혀 인지하지 못한다.
- HttpServletRequest를 Model로 사용한다.
- request가 제공하는 setAttribute() 를 사용하면 request 객체에 데이터를 보관해서 뷰에 전달할 수
있다.
- 뷰는 request.getAttribute() 를 사용해서 데이터를 꺼내면 된다.
- <%= request.getAttribute("member")%> 로 모델에 저장한 member 객체를 꺼낼 수 있지만, 너무
복잡해진다.
- JSP는 ${} 문법을 제공하는데, 이 문법을 사용하면 request의 attribute에 담긴 데이터를 편리하게 조회할 수 있다.
- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>를 선언하고 <c:forEach> 기능을 사용할 수 있다.
#### code
	<c:forEach var="item" items="${members}">
		<tr>
			<td>${item.id}</td>
			<td>${item.username}</td>
			<td>${item.age}</td>
		</tr>
	</c:forEach>
### MVC 패턴 - 한계
1. 포워드 중복
View로 이동하는 코드가 항상 중복 호출되어야 한다. 물론 이 부분을 메서드로 공통화해도 되지만, 해당
메서드도 항상 직접 호출해야 한다
	<pre><code>RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
	dispatcher.forward(request, response); </code></pre>
2. ViewPath에 중복
	<pre><code>String viewPath = "/WEB-INF/views/new-form.jsp";</code></pre>
3. 사용하지 않는 코드
	<pre><code>HttpServletRequest request, HttpServletResponse response</code></pre>
4. 공통 처리가 어렵다.
기능이 복잡해질 수 록 컨트롤러에서 공통으로 처리해야 하는 부분이 점점 더 많이 증가할 것이다. 단순히
공통 기능을 메서드로 뽑으면 될 것 같지만, 결과적으로 해당 메서드를 항상 호출해야 하고, 실수로 호출하지
않으면 문제가 될 것이다. 그리고 호출하는 것 자체도 중복이다.
5. 정리하면 공통 처리가 어렵다는 문제가 있다.
이 문제를 해결하려면 컨트롤러 호출 전에 먼저 공통 기능을 처리해야 한다. 소위 수문장 역할을 하는 기능이
필요하다. 프론트 컨트롤러(Front Controller) 패턴을 도입하면 이런 문제를 깔끔하게 해결할 수 있다.
스프링 MVC의 핵심도 바로 이 프론트 컨트롤러에 있다.
## MVC 프레임워크 만들기
### FrontController
![frontController](https://user-images.githubusercontent.com/48059565/136824909-fca796a6-d109-4597-b707-4e922743a3b2.jpg)

#### 특징
1. 프론트 컨트롤러 서블릿 하나로 클라이언트 요청을 받음
2. 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출한다.
3. 공통 처리를 수행한다.
4. 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 된다.
#### 스프링 웹 MVC와 프론트 컨트롤러
스프링 웹 MVC의 DispatcherServlet이 FrontController 패턴으로 구현되어 있음
### 프론트 컨트롤러 도입
프론트 컨트롤러에서 요청받은 URL에 해당하는 컨트롤러를 찾아 호출한다.
### View 분리
각 세부 컨트롤러는 기능에 맞는 View를 반환하고View를 호출하는 공통적인 부분을 프론트 컨트롤러 영역에서 처리한다.
### Model 추가
1. 컨트롤러가 사용하지 않는 서블릿 기술(HttpServletRequest, HttpServletResponse) 를 쓰지 않기 위해 request 객체를 Model로 사용한다. 서블릿 종속성이 제거된다.
2. 세부 컨트롤러에서는 뷰의 논리 이름만 반환하고 프론트 컨트롤러에서 뷰 리졸버를 통해 prefix, suffix를 붙혀 물리적인 이름으로 변환해준다.
### 단순하고 실용적인 컨트롤러
모델 객체를 프론트 컨트롤러에서 생성해서 넘겨준다. 컨트롤러에서 모델 객체에 값을 담으면 여기에
그대로 담겨있게 된다.
	<pre><code>Map<String, Object> model = new HashMap<>();</code></pre>
### 유연한 컨트롤러
어떤 개발자는 ControllerV3 방식으로 개발하고 싶고, 어떤 개발자는 ControllerV4 방식으로
개발하고 싶은 경우
#### 어댑터 패턴
어댑터 패턴을 사용해서 프론트 컨트롤러가 다양한 방식의 컨트롤러를 처리할 수 있다.
#### 유연한 컨트롤러 구조
![v5](https://user-images.githubusercontent.com/48059565/136826861-970356e3-96c2-4150-87fc-b49a66445749.jpg)
- 핸들러 어댑터 : 
어댑터 역할을 통해 다양한 종류의 컨트롤러를 호출할 수 있다.
- 핸들러: 어댑터를 통해 컨트롤러의 뿐만 아니라 어떠한 것이든 해당하는 종류의 어댑터만 있으면 다 처리할 수 있다.
- 어댑터는 실제 컨트롤러를 호출하고, 그 결과로 ModelView를 반환해야 한다.
- 실제 컨트롤러가 ModelView를 반환하지 못하면, 어댑터가 ModelView를 직접 생성해서라도 반환해야 한다.
- 이전에는 프론트 컨트롤러가 실제 컨트롤러를 호출했지만 이제는 이 어댑터를 통해서 실제 컨트롤러가 호출된다.
## 스프링 MVC - 구조 이해
###  SpringMVC 구조
![springmvc](https://user-images.githubusercontent.com/48059565/136827516-e338ea3a-10b9-4b41-af23-87200c5989c5.jpg)
#### 동작순서
1. 핸들러 조회: 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
2. 핸들러 어댑터 조회: 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.
3. 핸들러 어댑터 실행: 핸들러 어댑터를 실행한다.
4. 핸들러 실행: 핸들러 어댑터가 실제 핸들러를 실행한다.
5. ModelAndView 반환: 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 변환해서
반환한다.
6. viewResolver 호출: 뷰 리졸버를 찾고 실행한다.
	- JSP의 경우: InternalResourceViewResolver 가 자동 등록되고, 사용된다.
7. View 반환: 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를
반환한다.
	- JSP의 경우 InternalResourceView(JstlView) 를 반환하는데, 내부에 forward() 로직이 있다.
8. 뷰 렌더링: 뷰를 통해서 뷰를 렌더링 한다.

## 스프링 MVC - 시작하기

#### @Controller :
스프링이 자동으로 스프링 빈으로 등록한다. (내부에 @Component 애노테이션이 있어서 컴포넌트
스캔의 대상이 됨)
스프링 MVC에서 애노테이션 기반 컨트롤러로 인식한다.
#### @RequestMapping : 
요청 정보를 매핑한다. 해당 URL이 호출되면 이 메서드가 호출된다. 애노테이션을
기반으로 동작하기 때문에, 메서드의 이름은 임의로 지으면 된다.
#### 클래스 레벨에 다음과 같이 @RequestMapping 을 두면 메서드 레벨과 조합이 된다.
- 클래스 레벨 @RequestMapping("/springmvc/v2/members")
	- 메서드 레벨 @RequestMapping("/new-form") => /springmvc/v2/members/new-form
	- 메서드 레벨 @RequestMapping("/save") => /springmvc/v2/members/save
	- 메서드 레벨 @RequestMapping => /springmvc/v2/members

## 스프링 MVC - 기본 기능
### 로깅
	log.trace("trace log={}", name);
	log.debug("debug log={}", name);
	log.info(" info log={}", name);
	log.warn(" warn log={}", name);
	log.error("error log={}", name);

LEVEL: TRACE > DEBUG > INFO > WARN > ERROR
개발 서버는 debug 출력
운영 서버는 info 출력


### 요청 매핑
#### @RestController : 
@Controller 는 반환 값이 String 이면 뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 랜더링 된다.
@RestController 는 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력한다.
따라서 실행 결과로 ok 메세지를 받을 수 있다. @ResponseBody 와 관련이 있는데, 뒤에서 더 자세히
설명한다.
#### @RequestMapping("/hello-basic") : 
/hello-basic URL 호출이 오면 이 메서드가 실행되도록 매핑한다.
대부분의 속성을 배열[] 로 제공하므로 다중 설정도 가능하다. {"/hello-basic", "/hello-go"}
#### HTTP 메서드 매핑 축약
	/**
	* 편리한 축약 애노테이션 (코드보기)
	* @GetMapping
	* @PostMapping
	* @PutMapping
	* @DeleteMapping
	* @PatchMapping
	*/
	@GetMapping(value = "/mapping-get-v2")
	public String mappingGetV2() {
		log.info("mapping-get-v2");
		return "ok";
	}
#### PathVariable(경로 변수) 사용
	* PathVariable 사용
	* 변수명이 같으면 생략 가능
	* @PathVariable("userId") String userId -> @PathVariable userId
	*/
	@GetMapping("/mapping/{userId}")
	public String mappingPath(@PathVariable("userId") String data) {
		log.info("mappingPath userId={}", data);
		return "ok";
	}
### HTTP 요청 - 기본, 헤더 조회
	public String headers(HttpServletRequest request,
										HttpServletResponse response,
										HttpMethod httpMethod,
										Locale locale,
										@RequestHeader MultiValueMap<String, String>
										headerMap,
										@RequestHeader("host") String host,
										@CookieValue(value = "myCookie", required = false)
										String cookie
	) { ... }
애노테이션 기반의 스프링 컨트롤러는 다양한 파라미터를 지원한다.

### HTTP 요청 파라미터 - @RequestParam
	/**
	* @RequestParam 사용
	* - 파라미터 이름으로 바인딩
	* - HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
	* - required 옵션으로 필수 여부 설정 가능
	* @ResponseBody 추가
	* - View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
	*/
	@ResponseBody
	@RequestMapping("/request-param-v2")
	public String requestParamV2(
		@RequestParam("username") String memberName,
		@RequestParam("age") int memberAge) {
		log.info("username={}, age={}", memberName, memberAge);
		return "ok";
	}
스프링이 제공하는 @RequestParam 을 사용하면 요청 파라미터를 매우 편리하게 사용할 수 있다.
#### @RequestParam : 
파라미터 이름으로 바인딩
#### @ResponseBody : 
View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
@RestController와 같은 역할

### HTTP 요청 파라미터 - @ModelAttribute
	/**
	* @ModelAttribute 사용
	* 참고: model.addAttribute(helloData) 코드도 함께 자동 적용됨, 뒤에 model을 설명할 때
	자세히 설명
	*/
	@ResponseBody
	@RequestMapping("/model-attribute-v1")
	public String modelAttributeV1(@ModelAttribute HelloData helloData) {
		log.info("username={}, age={}", helloData.getUsername(),
		helloData.getAge());
		return "ok";
	}
스프링MVC는 @ModelAttribute 가 있으면 다음을 실행한다.
1. HelloData 객체를 생성한다.
2. 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 setter를
호출해서 파라미터의 값을 입력(바인딩) 한다.
예) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.

### HTTP 요청 메시지 - 단순 텍스트
요청 파라미터와 다르게, HTTP 메시지 바디를 통해 데이터가 직접 데이터가 넘어오는 경우는
@RequestParam , @ModelAttribute 를 사용할 수 없다.

	/**
	* @RequestBody
	* - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
	* - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
	*
	* @ResponseBody
	* - 메시지 바디 정보 직접 반환(view 조회X)
	* - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
	*/
	@ResponseBody
	@PostMapping("/request-body-string-v4")
	public String requestBodyStringV4(@RequestBody String messageBody) {
		log.info("messageBody={}", messageBody);
		return "ok";
	}
#### @RequestBody : 
@RequestBody 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다. 참고로 헤더 정보가
필요하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.
이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam ,
@ModelAttribute 와는 전혀 관계가 없다.
#### 요청 파라미터 vs HTTP 메시지 바디
- 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
- HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody
#### @ResponseBody
@ResponseBody 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.
물론 이 경우에도 view를 사용하지 않는다.

### HTTP 요청 메시지 - JSON
	/**
	* @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버림)
	* HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (contenttype:
	application/json)
	*
	* @ResponseBody 적용
	* - 메시지 바디 정보 직접 반환(view 조회X)
	* - HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter 적용
	(Accept: application/json)
	*/
	@ResponseBody
	@PostMapping("/request-body-json-v5")
	public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
		log.info("username={}, age={}", data.getUsername(), data.getAge());
		return data;
	}
- @RequestBody 요청
	- JSON 요청 HTTP 메시지 컨버터 객체
- @ResponseBody 응답
	- 객체 HTTP 메시지 컨버터 JSON 응답

### HTTP 응답 - 정적 리소스, 뷰 템플릿
- 정적 리소스
	- 예) 웹 브라우저에 정적인 HTML, css, js을 제공할 때는, 정적 리소스를 사용한다.
- 뷰 템플릿 사용
	- 예) 웹 브라우저에 동적인 HTML을 제공할 때는 뷰 템플릿을 사용한다.
- HTTP 메시지 사용
	- HTTP API를 제공하는 경우에는 HTML이 아니라 데이터를 전달해야 하므로, HTTP 메시지 바디에 JSON 같은 형식으로 데이터를 실어 보낸다.

### HTTP 메시지 컨버터
#### @ResponseBody 를 사용 : 
- HTTP의 BODY에 문자 내용을 직접 반환
- viewResolver 대신에 HttpMessageConverter 가 동작
- 기본 문자처리: StringHttpMessageConverter
- 기본 객체처리: MappingJackson2HttpMessageConverter
- byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음
#### HTTP 요청 데이터 읽기 : 
1. HTTP 요청이 오고, 컨트롤러에서 @RequestBody , HttpEntity 파라미터를 사용한다.
2. 메시지 컨버터가 메시지를 읽을 수 있는지 확인하기 위해 canRead() 를 호출한다.
	- 대상 클래스 타입을 지원하는가.
		- 예) @RequestBody 의 대상 클래스 ( byte[] , String , HelloData )
	- HTTP 요청의 Content-Type 미디어 타입을 지원하는가.
		- 예) text/plain , application/json , */*
3. canRead() 조건을 만족하면 read() 를 호출해서 객체 생성하고, 반환한다.
#### HTTP 응답 데이터 생성
1. 컨트롤러에서 @ResponseBody , HttpEntity 로 값이 반환된다.
2. 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 canWrite() 를 호출한다.
	- 대상 클래스 타입을 지원하는가.
		- 예) return의 대상 클래스 ( byte[] , String , HelloData )
	- HTTP 요청의 Accept 미디어 타입을 지원하는가.(더 정확히는 @RequestMapping 의 produces )
		- 예) text/plain , application/json , */*
3. canWrite() 조건을 만족하면 write() 를 호출해서 HTTP 응답 메시지 바디에 데이터를 생성한다.

#### RequestMappingHandlerAdapter 동작 방식
![requestmappingHanddlerAdapter](https://user-images.githubusercontent.com/48059565/136985564-cc218453-b955-4797-828c-19d2cef1c3f5.jpg)
#### ArgumentResolver
애노테이션 기반 컨트롤러를 처리하는 RequestMappingHandlerAdaptor 는 바로 이
ArgumentResolver 를 호출해서 컨트롤러(핸들러)가 필요로 하는 다양한 파라미터의 값(객체)을 생성한다.
그리고 이렇게 파리미터의 값이 모두 준비되면 컨트롤러를 호출하면서 값을 넘겨준다.
#### HTTP 메시지 컨버터 위치
![messageCv](https://user-images.githubusercontent.com/48059565/136985893-f8bfe58b-bc42-4401-ae33-d20d599e5f70.jpg)
#### 요청의 경우 
@RequestBody 를 처리하는 ArgumentResolver 가 있고, HttpEntity 를 처리하는
ArgumentResolver 가 있다. 이 ArgumentResolver 들이 HTTP 메시지 컨버터를 사용해서 필요한
객체를 생성하는 것이다. 
#### 응답의 경우 
@ResponseBody 와 HttpEntity 를 처리하는 ReturnValueHandler 가 있다. 그리고
여기에서 HTTP 메시지 컨버터를 호출해서 응답 결과를 만든다.
## 타임리프
#### 타임리프 사용 선언
	<html xmlns:th="http://www.thymeleaf.org">
#### 속성 변경 - th:href
	th:href="@{/css/bootstrap.min.css}"
- href="value1" 을 th:href="value2" 의 값으로 변경한다.
- 타임리프 뷰 템플릿을 거치게 되면 원래 값을 th:xxx 값으로 변경한다. 만약 값이 없다면 새로 생성한다.
- HTML을 그대로 볼 때는 href 속성이 사용되고, 뷰 템플릿을 거치면 th:href 의 값이 href 로 대체되면서 동적으로 변경할 수 있다.
- 대부분의 HTML 속성을 th:xxx 로 변경할 수 있다.
#### 타임리프 핵심
- 핵심은 th:xxx 가 붙은 부분은 서버사이드에서 렌더링 되고, 기존 것을 대체한다. th:xxx 이 없으면 기존 html의 xxx 속성이 그대로 사용된다.
- HTML을 파일로 직접 열었을 때, th:xxx 가 있어도 웹 브라우저는 th: 속성을 알지 못하므로 무시한다.
- 따라서 HTML을 파일 보기를 유지하면서 템플릿 기능도 할 수 있다
#### 속성 변경 - th:onclick
	onclick="location.href='addForm.html'"
	th:onclick="|location.href='@{/basic/items/add}'|"
#### 리터럴 대체 - |...|
- 타임리프에서 문자와 표현식 등은 분리되어 있기 때문에 더해서 사용해야 한다.
	
		<span th:text="'Welcome to our application, ' + ${user.name} + '!'">
- 다음과 같이 리터럴 대체 문법을 사용하면, 더하기 없이 편리하게 사용할 수 있다.
	
		<span th:text="|Welcome to our application, ${user.name}!|">

#### 반복 출력 - th:each
	<tr th:each="item : ${items}">
		<td><a href="item.html" th:href="@{/basic/items/{itemId}(itemId=${item.id})}" th:text="${item.id}">회원id</a></td>
		<td><a href="item.html" th:href="@{|/basic/items/${item.id}|}"th:text="${item.itemName}">상품명</a></td>
		<td th:text="${item.price}">10000</td>
		<td th:text="${item.quantity}">10</td>
	</tr>

#### 변수 표현식 - ${...}
	<td th:text="${item.price}">10000</td>
- 모델에 포함된 값이나, 타임리프 변수로 선언한 값을 조회할 수 있다.
- 프로퍼티 접근법을 사용한다. ( item.getPrice() )
#### 내용 변경 - th:text
	<td th:text="${item.price}">10000</td>
- 내용의 값을 th:text 의 값으로 변경한다.
#### URL 링크 표현식2 - @{...},
	th:href="@{/basic/items/{itemId}(itemId=${item.id})}"
#### URL 링크 간단히
	th:href="@{|/basic/items/${item.id}|}"
- 리터럴 대체 문법을 활용해서 간단히 사용할 수도 있다.

## PRG Post/Redirect/Get
![PRG](https://user-images.githubusercontent.com/48059565/136988143-3be0de73-aaab-4bc1-bbd9-cb3ef6d8aed3.jpg)
웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.
새로 고침 문제를 해결하려면 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라, 상품 상세 화면으로
리다이렉트를 호출해주면 된다. (URL을 바꾸어 Controller 단에서 부터 다시 동작한다.)
웹 브라우저는 리다이렉트의 영향으로 상품 저장 후에 실제 상품 상세 화면으로 다시 이동한다. 따라서
마지막에 호출한 내용이 상품 상세 화면인 GET /items/{id} 가 되는 것이다.
이후 새로고침을 해도 상품 상세 화면으로 이동하게 되므로 새로 고침 문제를 해결할 수 있다.
#### RedirectAttributes
	"redirect:/basic/items/" + item.getId()
item.getId()의 인코딩 문제 해결

	/**
	* RedirectAttributes
	*/
	@PostMapping("/add")
	public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/basic/items/{itemId}";
	}
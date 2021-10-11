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

package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute(item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String save(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam int quantity,
                       Model model){
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute("item") Item item, Model model){
        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item){
        itemRepository.save(item);
        // @ModelAttribute 어노테이션의 name 속성으로 model에 알아서 담아준다.
        //model.addAttribute("item", item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, Model model){
        itemRepository.save(item);
        // @ModelAttribute 어노테이션의 name 속성으로 model에 알아서 담아준다.
        // name을 주지 않으면 클래스명의 첫글자를 소문자로 바꿔서 담아준다. Item -> item
        //model.addAttribute("item", item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV4(Item item, Model model){
        itemRepository.save(item);
        // @ModelAttribute 어노테이션의 name 속성으로 model에 알아서 담아준다.
        // name을 주지 않으면 클래스명의 첫글자를 소문자로 바꿔서 담아준다. Item -> item
        // 파라미터를 객체로 받으면 @ModelAttribute 어노테이션이 자동 적용된다.
        //model.addAttribute("item", item);
        return "basic/item";
    }

    @PostMapping("/add")
    public String addItemV5(Item item, Model model){
        itemRepository.save(item);
        // @ModelAttribute 어노테이션의 name 속성으로 model에 알아서 담아준다.
        // name을 주지 않으면 클래스명의 첫글자를 소문자로 바꿔서 담아준다. Item -> item
        // 파라미터를 객체로 받으면 @ModelAttribute 어노테이션이 자동 적용된다.
        //model.addAttribute("item", item);
        return "redirect:/basic/items/" + item.getId();
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item, Model model) {
        itemRepository.update(itemId, item);
        model.addAttribute("item", item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}

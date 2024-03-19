package com.rymcu.forest.web.api.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.dto.admin.TagDTO;
import com.rymcu.forest.dto.admin.TopicTagDTO;
import com.rymcu.forest.dto.admin.UserRoleDTO;
import com.rymcu.forest.entity.*;
import com.rymcu.forest.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiresRoles(value = {"blog_admin", "admin"}, logical = Logical.OR)
public class AdminController {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private TopicService topicService;
    @Resource
    private TagService tagService;
    @Resource
    private SpecialDayService specialDayService;
    @Resource
    private ArticleService articleService;
    @Resource
    private CommentService commentService;
    @Resource
    private ProductService productService;

    @GetMapping("/users")
    public GlobalResult<PageInfo<UserInfoDTO>> users(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, UserSearchDTO searchDTO) {
        PageHelper.startPage(page, rows);
        List<UserInfoDTO> list = userService.findUsers(searchDTO);
        PageInfo<UserInfoDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/user/{idUser}/role")
    public GlobalResult<List<Role>> userRole(@PathVariable Long idUser) {
        List<Role> roles = roleService.findByIdUser(idUser);
        return GlobalResultGenerator.genSuccessResult(roles);
    }

    @GetMapping("/roles")
    public GlobalResult<PageInfo<Role>> roles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<Role> list = roleService.findAll();
        PageInfo<Role> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @PatchMapping("/user/update-role")
    public GlobalResult<Boolean> updateUserRole(@RequestBody UserRoleDTO userRole) throws ServiceException {
        boolean flag = userService.updateUserRole(userRole.getIdUser(), userRole.getIdRole());
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PatchMapping("/user/update-status")
    public GlobalResult<Boolean> updateUserStatus(@RequestBody User user) throws ServiceException {
        boolean flag = userService.updateStatus(user.getIdUser(), user.getStatus());
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PatchMapping("/role/update-status")
    public GlobalResult<Boolean> updateRoleStatus(@RequestBody Role role) throws ServiceException {
        boolean flag = roleService.updateStatus(role.getIdRole(), role.getStatus());
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PostMapping("/role/post")
    public GlobalResult<Boolean> addRole(@RequestBody Role role) throws ServiceException {
        boolean flag = roleService.saveRole(role);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PutMapping("/role/post")
    public GlobalResult<Boolean> updateRole(@RequestBody Role role) throws Exception {
        boolean flag = roleService.saveRole(role);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @GetMapping("/topics")
    public GlobalResult<PageInfo<Topic>> topics(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<Topic> list = topicService.findAll();
        PageInfo<Topic> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/topic/{topicUri}")
    public GlobalResult topic(@PathVariable String topicUri) {
        if (StringUtils.isBlank(topicUri)) {
            throw new IllegalArgumentException("参数异常!");
        }
        Topic topic = topicService.findTopicByTopicUri(topicUri);
        return GlobalResultGenerator.genSuccessResult(topic);
    }

    @GetMapping("/topic/{topicUri}/tags")
    public GlobalResult<PageInfo<TagDTO>> topicTags(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, @PathVariable String topicUri) {
        if (StringUtils.isBlank(topicUri)) {
            throw new IllegalArgumentException("参数异常!");
        }
        PageHelper.startPage(page, rows);
        List<TagDTO> list = topicService.findTagsByTopicUri(topicUri);
        PageInfo<TagDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/topic/detail/{idTopic}")
    public GlobalResult<Topic> topicDetail(@PathVariable Integer idTopic) {
        Topic topic = topicService.findById(idTopic.toString());
        return GlobalResultGenerator.genSuccessResult(topic);
    }

    @GetMapping("/topic/unbind-topic-tags")
    public GlobalResult<PageInfo<Tag>> unbindTopicTags(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, HttpServletRequest request) {
        Long idTopic = Long.valueOf(request.getParameter("idTopic"));
        String tagTitle = request.getParameter("tagTitle");
        PageHelper.startPage(page, rows);
        List<Tag> list = topicService.findUnbindTagsById(idTopic, tagTitle);
        PageInfo<Tag> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/topic/bind-topic-tag")
    public GlobalResult bindTopicTag(@RequestBody TopicTagDTO topicTag) throws Exception {
        TopicTagDTO newTopicTagDTO = topicService.bindTopicTag(topicTag);
        return GlobalResultGenerator.genSuccessResult(newTopicTagDTO);
    }

    @DeleteMapping("/topic/unbind-topic-tag")
    public GlobalResult unbindTopicTag(@RequestBody TopicTagDTO topicTag) throws Exception {
        TopicTagDTO topicTagDTO = topicService.unbindTopicTag(topicTag);
        return GlobalResultGenerator.genSuccessResult(topicTagDTO);
    }

    @PostMapping("/topic/post")
    public GlobalResult<Topic> addTopic(@RequestBody Topic topic) throws Exception {
        Topic newTopic = topicService.saveTopic(topic);
        return GlobalResultGenerator.genSuccessResult(newTopic);
    }

    @PutMapping("/topic/post")
    public GlobalResult<Topic> updateTopic(@RequestBody Topic topic) throws Exception {
        Topic newTopic = topicService.saveTopic(topic);
        return GlobalResultGenerator.genSuccessResult(newTopic);
    }

    @GetMapping("/tags")
    public GlobalResult<PageInfo<Tag>> tags(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<Tag> list = tagService.findAll();
        PageInfo<Tag> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @DeleteMapping("/tag/clean-unused")
    public GlobalResult cleanUnusedTag() {
        boolean bool = tagService.cleanUnusedTag();
        return GlobalResultGenerator.genSuccessResult(bool);
    }

    @GetMapping("/tag/detail/{idTag}")
    public GlobalResult<Tag> tagDetail(@PathVariable Integer idTag) {
        Tag tag = tagService.findById(idTag.toString());
        return GlobalResultGenerator.genSuccessResult(tag);
    }

    @PostMapping("/tag/post")
    public GlobalResult<Tag> addTag(@RequestBody Tag tag) throws Exception {
        Tag newTag = tagService.saveTag(tag);
        return GlobalResultGenerator.genSuccessResult(newTag);
    }

    @PutMapping("/tag/post")
    public GlobalResult<Tag> updateTag(@RequestBody Tag tag) throws Exception {
        Tag newTag = tagService.saveTag(tag);
        return GlobalResultGenerator.genSuccessResult(newTag);
    }

    @GetMapping("/special-days")
    public GlobalResult<PageInfo<SpecialDay>> specialDays(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<SpecialDay> list = specialDayService.findAll();
        PageInfo<SpecialDay> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/articles")
    public GlobalResult<PageInfo<ArticleDTO>> articles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, ArticleSearchDTO articleSearchDTO) {
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findArticles(articleSearchDTO);
        PageInfo<ArticleDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/comments")
    public GlobalResult<PageInfo<CommentDTO>> comments(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<CommentDTO> list = commentService.findComments();
        PageInfo<CommentDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/products")
    public GlobalResult<PageInfo<ProductDTO>> products(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<ProductDTO> list = productService.findProducts();
        PageInfo<ProductDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }


}

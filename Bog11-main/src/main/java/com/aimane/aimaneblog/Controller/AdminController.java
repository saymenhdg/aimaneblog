package com.aimane.aimaneblog.Controller;

import com.aimane.aimaneblog.Model.Post;
import com.aimane.aimaneblog.Model.User;
import com.aimane.aimaneblog.Service.PostService;
import com.aimane.aimaneblog.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Controller
public class AdminController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public AdminController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        return "admindashboard"; // Ensure this view exists
    }

    @PostMapping("/admin/posts")
    public String createPost(@RequestParam String title,
                             @RequestParam String content,
                             @RequestParam(required = false) MultipartFile postImg,
                             HttpSession session, Model model) {
        try {
            // Retrieve userId from the session
            Long userId = (Long) session.getAttribute("userId");

            // Check if the user is logged in and is an admin
            if (userId == null) {
                model.addAttribute("error", "You must be logged in to create a post.");
                return "login";  // Redirect to login page if not logged in
            }

            if (userService.isAdmin(userId)) {
                User user = userService.getUserById(userId).orElse(null);
                if (user != null) {
                    // Create and save the post
                    Post post = new Post();
                    post.setTitle(title);
                    post.setContent(content);
                    post.setUser(user);
                    post.setCreatedDate(LocalDateTime.now());

                    // Handle image upload if available
                    if (postImg != null && !postImg.isEmpty()) {
                        try {
                            post.setPostImg(postImg.getBytes());
                        } catch (IOException e) {
                            model.addAttribute("error", "Error uploading the image. Please try again.");
                            return "admindashboard";
                        }
                    }

                    postService.savePost(post);  // Save the post to the database
                    return "redirect:/admin/dashboard";  // Redirect to admin dashboard after saving the post
                } else {
                    model.addAttribute("error", "User not found.");
                    return "admindashboard";
                }
            } else {
                model.addAttribute("error", "You must be an admin to create posts.");
                return "admindashboard";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred while creating the post.");
            return "admindashboard";
        }
    }


}

package com.prollhub.community.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RoutingController {

    // This regex reroutes to any unknown paths
    // todo validate that none are existing & leading slash required
    private static final String ANY_PATH =  "/{path:^(?!api|static|.*\\.[^.]+$).*$}/**";

    @Value("${app.base-domain}")
    private String baseDomain;

    // Root view (index)
    @RequestMapping(value = {"/", ANY_PATH}) // Match root and deep links not matching known assets/api
    public String handleRootOrSubdomain(HttpServletRequest request, Model model) {
        String host = request.getServerName();

        // check any subdomains
        if (host != null && !baseDomain.isEmpty() && host.endsWith("." + baseDomain)) {
            String subdomain = host.substring(0, host.indexOf("." + baseDomain));

            if (!subdomain.isEmpty() && !subdomain.equalsIgnoreCase("www")) {
                return "/profile-view.html";
            }
        }

        return "index.html";
    }

    // Legal view
    @RequestMapping(value = {"/legal", "/legal" + ANY_PATH})
    public String forwardLegal() {
        return "/legal.html";
    }

    // Dashboard view
    @RequestMapping(value = {"/dashboard", "/dashboard" + ANY_PATH})
    public String forwardDashboard() {
        return "/dashboard.html";
    }

    // Content Manager view
    @RequestMapping(value = {"/content-manager", "/content-manager" +  ANY_PATH})
    public String forwardContentManager() {
        return "/content-manager.html";
    }

    // Profile Manager view - Served from /static/profile-manager.html
    @RequestMapping(value = {"/profile", "/profile" + ANY_PATH})
    public String forwardProfileManager() {
        return "/profile-manager.html";
    }

    // Account Manager view - Served from /static/account-manager.html
    @RequestMapping(value = {"/account/", "/account/" + ANY_PATH})
    public String forwardAccountManager() {
        return "/account-manager.html";
    }

    // --- Admin Views ---
    @RequestMapping(value = {"/admin", "/admin"  + ANY_PATH})
    public String forwardAdmin() {
        return "/admin.html";
    }

    // User Manager view - Served from /static/user-manager.html
    @RequestMapping(value = {"/users/{username}", "/users/{username}" + ANY_PATH})
    public String forwardUserManager(@PathVariable String username) {
        // todo retrieve profile information
        return "/profile-view.html";
    }


}

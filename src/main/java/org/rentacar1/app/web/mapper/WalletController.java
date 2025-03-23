package org.rentacar1.app.web.mapper;

import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.service.UserService;
import org.rentacar1.app.wallet.model.Wallet;
import org.rentacar1.app.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@Controller
public class WalletController {
    private final WalletService walletService;
    private final UserService userService;

    @Autowired
    public WalletController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping("/wallet")
    public ModelAndView getWalletPage(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Wallet wallet = walletService.getWalletByUserId(user.getId());

        ModelAndView modelAndView = new ModelAndView("wallet");
        modelAndView.addObject("wallet", wallet);
        return modelAndView;
    }

    @PostMapping("/wallet/add")
    public String addFunds(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("amount") BigDecimal amount) {
        User user = userService.findByUsername(userDetails.getUsername());
        walletService.addFunds(user.getId(), amount);

        return "redirect:/wallet";
    }
}

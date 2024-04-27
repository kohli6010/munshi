package com.techxnet.munshi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techxnet.munshi.dtos.SendOtpRequestDto;
import com.techxnet.munshi.dtos.VerifyOtpRequestDto;
import com.techxnet.munshi.models.Otp;
import com.techxnet.munshi.services.OtpService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class otpController {
    @Autowired
    private OtpService otpService;

    @PostMapping("/customer/send-otp")
    public Otp sendOtp(@RequestBody SendOtpRequestDto body) {
        Otp otp = otpService.sendOtp(body.getMobile());
        return otp;
    }
    
    @PostMapping("/customer/{id}/verify-otp")
    public String verifyOtp(@PathVariable int id, @RequestBody VerifyOtpRequestDto body) {
        String message = otpService.verifyOtp(id, body.getOtp());
        return message;
    }
    
}

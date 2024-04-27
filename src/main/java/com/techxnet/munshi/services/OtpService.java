package com.techxnet.munshi.services;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techxnet.munshi.daos.CustomerRepository;
import com.techxnet.munshi.daos.OtpRepository;
import com.techxnet.munshi.exceptions.OtpException;
import com.techxnet.munshi.models.Customer;
import com.techxnet.munshi.models.Otp;

@Service
public class OtpService {
    @Autowired
    private OtpRepository otpRepo;

    @Autowired
    private CustomerRepository customerRepo;

    private Otp setOtp(Customer customer) {
        int otp = generateOTP();
        
        Otp newOtp = new Otp(otp, customer);
        newOtp.setId(0);

        LocalDateTime curreDateTime = LocalDateTime.now();
        LocalDateTime futureDateTime = curreDateTime.plusMinutes(10);
        Date expiresAt = Date.from(futureDateTime.atZone(ZoneId.systemDefault()).toInstant());

        newOtp.setExpiresAt(expiresAt);

        Otp savedOtp = otpRepo.save(newOtp);

        return savedOtp;
    }

    private int generateOTP() {
        StringBuilder otp = new StringBuilder();
        int length = 6;

        // Use SecureRandom for cryptographically secure random number generation
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            otp.append(digit);
        }
        
        while (otp.length() < length) {
            otp.insert(0, "0");
        }

        return Integer.parseInt(otp.toString());
    }

    public Otp sendOtp(String mobile) {
        Customer customer = new Customer();
        customer.setId(0);
        customer.setMobile(mobile);
        
        Otp otp = setOtp(customer);
        customer.getOtps().add(otp);

        customerRepo.save(customer);
        //Logic to send otp to customer.
        
        return otp;
    }

    public String verifyOtp(int customerId, int otp) throws OtpException {
        try {
            Otp dbOtp = otpRepo.findByCustomerIdOrderByCreatedAtDesc(customerId)
                    .orElseThrow(() -> new OtpException("No OTP found for the customer"));

            if(dbOtp.isUsed()) {
                throw new OtpException("OTP already used.");
            }

            if (dbOtp.getOtp() != otp) {
                throw new OtpException("Incorrect OTP");
            }

            boolean otpExpired = compareCurrentTimeWithExpiresAtTime(dbOtp);
            if (otpExpired) {
                throw new OtpException("OTP expired.");
            }

            dbOtp.setUsed(true);
            otpRepo.save(dbOtp);

            return "Otp verified";
        } catch (OtpException e) {
            throw e;
        } catch (Exception e) {
            throw new OtpException("Error verifying OTP", e);
        }
    }
    

    private boolean compareCurrentTimeWithExpiresAtTime(Otp otp) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Instant availableTimeStampInstant = otp.getExpiresAt().toInstant();
        LocalDateTime expiresAtDateTime = availableTimeStampInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        int comparisonTime = currentDateTime.compareTo(expiresAtDateTime);

        if(comparisonTime < 0) {
            return false;
        }

        return true;
    }
}

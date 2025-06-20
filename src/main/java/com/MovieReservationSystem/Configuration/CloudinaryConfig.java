package com.MovieReservationSystem.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dyz4itzmw",
                "api_key", "278669918872358",
                "api_secret", "lp0gsJri8XF5yVIGuP3pmRVCQ-E",
                "secure", true
        ));
    }
}

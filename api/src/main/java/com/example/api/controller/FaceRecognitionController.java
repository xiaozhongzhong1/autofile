package com.example.api.controller;

import com.example.api.service.IFaceRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人脸识别
 */
@RestController
public class FaceRecognitionController {
    @Autowired
    private IFaceRecognitionService faceRecognitionService;


    @GetMapping("/test")
    public void extract(){
        faceRecognitionService.extract();
    }
}

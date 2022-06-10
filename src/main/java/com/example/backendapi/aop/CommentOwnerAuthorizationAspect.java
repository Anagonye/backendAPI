package com.example.backendapi.aop;

import com.example.backendapi.comment.CommentDto;
import com.example.backendapi.comment.CommentService;
import com.example.backendapi.exceptions.CommentNotFoundException;
import com.example.backendapi.user.AppUser;
import com.example.backendapi.user.AppUserService;
import com.example.backendapi.user.AuthUtils;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Aspect
@Component
@AllArgsConstructor
public class CommentOwnerAuthorizationAspect {
    private final AppUserService appUserService;
    private final CommentService commentService;

    @Before("@annotation(authorization) && args(commentId,..)")
    public void ownerAuthorization(CommentOwnerAuthorization authorization, Long commentId){
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(AuthUtils.getCurrentPrincipal().getUsername());
        Long ownerId = commentService.findById(commentId)
                .map(CommentDto::getOwnerId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        if(!ownerId.equals(appUser.getId())){
            throw new AuthorizationServiceException("Permission denied");
        }
    }
}

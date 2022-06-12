package com.example.backendapi.aop;
import com.example.backendapi.exceptions.PostNotFoundException;
import com.example.backendapi.post.PostDto;
import com.example.backendapi.post.PostServiceImpl;
import com.example.backendapi.user.AuthUtils;
import lombok.AllArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;


@Aspect
@Component
@AllArgsConstructor
public class PostOwnerAuthorizationAspect {

    private final PostServiceImpl postService;


    @Before("@annotation(authorization) && args(postId,..)")
    public void ownerAuthorization(PostOwnerAuthorization authorization, Long postId){
        Long ownerId = postService.findById(postId)
                .map(PostDto::getOwnerId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        if(!ownerId.equals(AuthUtils.getCurrentPrincipal().getId())){
            throw new AuthorizationServiceException("Permission denied");
        }


    }
}

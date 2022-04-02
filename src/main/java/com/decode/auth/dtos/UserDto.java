package com.decode.auth.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    public interface UserView{
        public static interface ResgistrationPost{}
        public static interface UserPut{}
        public static interface PasswordPut{}
        public static interface ImagePut{}
    }
    private UUID userId;

    @NotBlank(groups = UserView.ResgistrationPost.class)
    @Size(min = 4, max = 50)
    @JsonView(UserView.ResgistrationPost.class)
    private String username;

    @NotBlank(groups = UserView.ResgistrationPost.class)
    @Email
    @JsonView(UserView.ResgistrationPost.class)
    private String email;

    @NotBlank(groups = {UserView.ResgistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 6, max = 20)
    @JsonView({UserView.ResgistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @NotBlank(groups = UserView.PasswordPut.class)
    @JsonView({UserView.PasswordPut.class})
    private String oldPassword;

    @JsonView({UserView.ResgistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.ResgistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView({UserView.ResgistrationPost.class, UserView.UserPut.class})
    private String cpf;

    @NotBlank(groups = UserView.ImagePut.class)
    @JsonView({UserView.ImagePut.class})
    private String imageUrl;
}

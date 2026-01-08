package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignupRequestDto {
    //users
    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 5, max = 20, message = "아이디는 4~20자여야 합니다.")
    private String id;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8,max = 20,message = "비밀번호는 8~20자여야 합니다.")
    private String pwd;
    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String pwdCheck;
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
//    @Pattern(
//            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
//            message = "이메일 형식이 올바르지 않습니다."
//    )
    @NotBlank(message = "이메일은 필수입니다.") //@Email은 null/빈문자열 허용 -> @NotBlank와 함께 써야함
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @Pattern(
            regexp = "^01[016789]-\\d{3,4}-\\d{4}$",
            message = "전화번호 형식이 올바르지 않습니다."
    )
    private String phone;
    @NotBlank(message = "주소는 필수입니다.")
    private String addr;
    @NotNull(message = "성별을 선택해 주세요.")
    private Integer gender;

    //account
    @NotBlank(message = "예금주는 필수입니다.")
    private String accountName;
    @NotNull(message = "은행을 선택해 주세요.")
    private String bank;
    @NotBlank(message = "계좌번호는 필수입니다.")
    private String account;

    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
    public boolean ispasswordMatched(){
        if(pwd ==null || pwdCheck == null){
            return false;
        }
        return pwd.equals(pwdCheck);
    }
}

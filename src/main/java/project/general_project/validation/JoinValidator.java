package project.general_project.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.general_project.web.form.memberForm.JoinForm;

@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return JoinForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String specialCharacters = "[!@#$%^&*(),.?\":{}|<>]";
        JoinForm joinForm = (JoinForm) target;
        if(!joinForm.getPassword().matches(".*"+specialCharacters+".*")||joinForm.getPassword().length()<10){
            errors.rejectValue("password","rule");
        }
        if(!joinForm.getPassword().equals(joinForm.getPasswordCheck())){
            errors.rejectValue("passwordCheck","passCheck");
        }
        if(!StringUtils.hasText(joinForm.getFirstPhone())||!StringUtils.hasText(joinForm.getSecondPhone())||!StringUtils.hasText(joinForm.getThirdPhone())){
            errors.rejectValue("firstPhone","phoneCheck");
        }
    }
}

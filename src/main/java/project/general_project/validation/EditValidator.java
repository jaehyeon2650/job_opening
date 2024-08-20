package project.general_project.validation;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import project.general_project.web.form.memberForm.EditForm;

@Component
public class EditValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return EditForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EditForm editForm = (EditForm) target;
        if(!StringUtils.hasText(editForm.getFirstPhone())||!StringUtils.hasText(editForm.getSecondPhone())||!StringUtils.hasText(editForm.getThirdPhone())){
            errors.rejectValue("firstPhone","phoneCheck");
        }

    }
}

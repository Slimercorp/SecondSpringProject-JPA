package ru.slimercorp.springcourse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.slimercorp.springcourse.models.Person;
import ru.slimercorp.springcourse.services.PeopleService;

import java.util.List;
import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        List<Person> personOptional = peopleService.findByName(person.getName());
        if (!personOptional.isEmpty()) {
            errors.rejectValue("name", "", "This person is already exist");
        }
    }
}

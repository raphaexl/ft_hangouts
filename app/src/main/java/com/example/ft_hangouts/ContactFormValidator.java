package com.example.ft_hangouts;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

public class ContactFormValidator {
    private EditText name_input, surname_input, tel_input, email_input, about_input, address_input;

    ContactFormValidator(EditText name_input, EditText surname_input, EditText tel_input, EditText email_input, EditText about_input, EditText address_input)
    {
        this.name_input = name_input;
        this.surname_input = surname_input;
        this.tel_input = tel_input;
        this.email_input = email_input;
        this.about_input = about_input;
        this.address_input = address_input;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean checkFormValidity(String name, String surname, String tel, String email, String about, String address){
        boolean has_error;

        has_error = false;
        if (name.isEmpty()){
            this.name_input.setError("Please valid First Name required");
            has_error = true;
        }
        if (tel.isEmpty() || !PhoneNumberUtils.isGlobalPhoneNumber(tel)){
            tel_input.setError("Please valid Phone Number required");
            has_error = true;
        }
        if (!email.isEmpty() && !isValidEmail(email)){
            email_input.setError("Please valid email address required");
            has_error = true;
        }
        return !has_error;
    }
}

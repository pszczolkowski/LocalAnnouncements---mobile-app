package pl.weeia.localannouncements.shared;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;

import java.util.List;

import pl.weeia.localannouncements.Application;
import pl.weeia.localannouncements.Container;

public abstract class BaseActivity extends AppCompatActivity implements ValidationListener {

    protected Validator validator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    protected Container getContainer() {
        return ((Application) getApplication()).getContainer();
    }

    protected void validate() {
        validator.validate();;
    }

    @Override
    public void onValidationSucceeded() {}

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError validationError : errors) {
            View view = validationError.getView();
            if (view instanceof EditText) {
                ((EditText) view).setError(validationError.getCollatedErrorMessage(this));
            }
        }
    }

}

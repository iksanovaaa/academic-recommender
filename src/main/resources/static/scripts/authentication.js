const UnderlineClass = 'underline';

class AuthenticationView extends View {
    constructor(corpus) {
        super();

        this._form = document.querySelector('#user-form');
        this._loginButton = document.querySelector('#login-button');
        this._registerButton = document.querySelector('#register-button');
        this._confirmWrapper = document.querySelector('#confirm-wrapper');
        this._passwordConfirmInput = document.querySelector('#password-confirm');
    }

    switchToRegister() {
        this._form.action = '/api/register';
        this._loginButton.classList.add(UnderlineClass);
        this._registerButton.classList.remove(UnderlineClass);
        this._confirmWrapper.hidden = false;
        this._passwordConfirmInput.required = true;
    }

    switchToLogin() {      
        this._form.action = '/api/login';  
        this._loginButton.classList.remove(UnderlineClass);
        this._registerButton.classList.add(UnderlineClass);
        this._confirmWrapper.hidden = true;
        this._passwordConfirmInput.required = false;
    }
}

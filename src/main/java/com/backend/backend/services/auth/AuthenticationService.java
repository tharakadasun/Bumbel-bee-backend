package com.backend.backend.services.auth;

import com.backend.backend.controllers.auth.AuthenticationRequest;
import com.backend.backend.controllers.auth.AuthenticationResponse;
import com.backend.backend.controllers.auth.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse Register(RegisterRequest request);

    AuthenticationResponse Authenticate(AuthenticationRequest request);
}

# Spring boot Security oAuth (using Github)

Basic Spring boot Security OAuth (using Github). Angular code is not covered here, but the calls are mocked using Thunderclient requests.

## Overview of Components

- Spring boot Backend Service (uses Github OAuth)
  - TokenStore: Generate token from/to authentication.
  - SecurityConfig.successHandler: Generate the token using the TokenStore call and return the token in the response to clients on auth success.
  - SecurityConfig.authenticationEntryPoint: Unauthenticated request will be shown 401 unauthorized error.
  - SecurityConfig.corsConfiguration: Allow XHR requests.
  - InMemoryRequestRepository: Use HashMap to hold the OAuthRequest in-memmory temporarily while the request is begin processed by Github.
  - HomeController.username: Get the username.
- Angular Client (Mocked via Thunderclient)
  - Login Component: Show the login button.
  - Callback Component: Github redirects to this callback URL.
  - Home Component: Access post login.
  - AuthGaurd: Check if the user is authenticated or not. Unauthenticated users redirected to login.
  - Header Interceptor: Put Auth HTTP header in each request.

## Setup Github

- Go to Github > Settings > Developer settings > OAuth Apps
- Click on 'Register a new application'. 
  - Set homepage URL to http://localhost:4200
  - Set callback URL to http://localhost:4200/callback
- You will be provided Client ID and Client secrets.

## Application Properties

- Refer `application.properties` for `client-id`, `client-secret` and `redirectUri`. 
- The `client-id` and `client-secret` is generated when you onboard your application on Github (steps given above).
- `redirectUri` is the callback url of your Angular application where `code-id` and `state` will be sent by Github oAuth.

## SecurityConfig Class

- `config.SecurityConfig` class extends `WebSecurityConfigurerAdapter` and uses `@EnableWebSecurity`.
- Configuration allows access to oAuth and login URLs without the need of authentication. Any other request to the URIs should be authenticated.
- Upon success, the auth object will be converted to JWT token via `TokenStore`class. Unauthenticated request will be shown 401 unauthorized error.
- The authorization requests are stored in-memory using HashMap for now by `config.InMemoryRequestRepository` class.

## Token Store Class

- We can define our own custom method to generate the JWTs. This is covered under `config.TokenStore` class.
- `config.SecurityConfig.successHandler()` (When oAuth is successful) calls `config.TokenStore` to create custom token store (generates JWT and store it in Hashmap cache).

## Token Filter Class

- The `TokenFilter` class extends `OncePerRequestFilter`. 
- It checks the authorization token in the header. 
- If the authorization token is found, it spilts the 'Bearer' part away.
- After this we check if the token is present in the Token Store. 
- If yes, we place it inside the security context.

## InMemoryRequestRepository Class

- Holds the OAuth request temporarily in-memory while the Github request is being made.
- If the request contains 'state' parameter, the logic removes previous cached value from the in-memory cache and returns the removed cache value.
- On the other hand `saveAuthorizationRequest` saves the authorizationRequest in the cache.

## HomeController Class



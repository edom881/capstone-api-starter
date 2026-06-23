package org.yearup.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.lang.reflect.Method;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProfileControllerTest
{
    @Test
    public void getProfile_shouldReturnProfileForLoggedInUser()
    {
        // arrange
        ProfileService profileService = mock(ProfileService.class);
        UserService userService = mock(UserService.class);
        Principal principal = mock(Principal.class);
        User user = new User(3, "george", "password", "ROLE_USER");
        Profile profile = createProfile(3, "George", "Jetson");

        when(principal.getName()).thenReturn("george");
        when(userService.getByUserName("george")).thenReturn(user);
        when(profileService.getByUserId(3)).thenReturn(profile);

        ProfileController controller = new ProfileController(profileService, userService);

        // act
        Profile actual = controller.getProfile(principal);

        // assert
        assertEquals(3, actual.getUserId(), "Controller should return the logged-in user's profile");
        assertEquals("George", actual.getFirstName(), "Profile should include first name");
    }

    @Test
    public void getProfile_withMissingProfile_shouldThrowNotFound()
    {
        // arrange
        ProfileService profileService = mock(ProfileService.class);
        UserService userService = mock(UserService.class);
        Principal principal = mock(Principal.class);
        User user = new User(3, "george", "password", "ROLE_USER");

        when(principal.getName()).thenReturn("george");
        when(userService.getByUserName("george")).thenReturn(user);
        when(profileService.getByUserId(3)).thenReturn(null);

        ProfileController controller = new ProfileController(profileService, userService);

        // act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.getProfile(principal));

        // assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "Missing profile should return 404");
    }

    @Test
    public void controller_shouldRequireLoggedInUser()
    {
        // act
        PreAuthorize annotation = ProfileController.class.getAnnotation(PreAuthorize.class);

        // assert
        assertEquals("isAuthenticated()", annotation.value(), "Profile should require a logged-in user");
    }

    @Test
    public void getProfile_shouldHaveGetMapping() throws NoSuchMethodException
    {
        // arrange
        Method method = ProfileController.class.getMethod("getProfile", Principal.class);

        // act
        GetMapping annotation = method.getAnnotation(GetMapping.class);

        // assert
        assertEquals(1, annotation.value().length, "Get profile should have a GET mapping");
        assertEquals("", annotation.value()[0], "Get profile should map to /profile");
    }

    private Profile createProfile(int userId, String firstName, String lastName)
    {
        return new Profile(userId, firstName, lastName, "800-555-9876",
                "george.jetson@email.com", "123 Birch Parkway", "Dallas", "TX", "75051");
    }
}

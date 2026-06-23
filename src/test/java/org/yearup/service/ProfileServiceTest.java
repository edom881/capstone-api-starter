package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Profile;
import org.yearup.repository.ProfileRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest
{
    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @Test
    public void getByUserId_withValidUserId_shouldReturnProfile()
    {
        // arrange
        Profile profile = createProfile(3, "George", "Jetson");

        when(profileRepository.findById(3)).thenReturn(Optional.of(profile));

        // act
        Profile actual = profileService.getByUserId(3);

        // assert
        assertEquals("George", actual.getFirstName(), "Should return the current user's profile");
        assertEquals("Jetson", actual.getLastName(), "Should include the profile details");
    }

    @Test
    public void getByUserId_withMissingUserId_shouldReturnNull()
    {
        // arrange
        when(profileRepository.findById(99999)).thenReturn(Optional.empty());

        // act
        Profile actual = profileService.getByUserId(99999);

        // assert
        assertNull(actual, "Missing profile should return null");
    }

    private Profile createProfile(int userId, String firstName, String lastName)
    {
        return new Profile(userId, firstName, lastName, "800-555-9876",
                "george.jetson@email.com", "123 Birch Parkway", "Dallas", "TX", "75051");
    }
}

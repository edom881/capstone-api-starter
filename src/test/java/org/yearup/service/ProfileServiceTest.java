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
import static org.mockito.Mockito.any;
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

    @Test
    public void update_withExistingProfile_shouldSaveProfileChanges()
    {
        // arrange
        Profile existing = createProfile(3, "George", "Jetson");
        Profile update = new Profile(99, "Jane", "Doe", "800-555-1111",
                "jane.doe@email.com", "987 Cedar Street", "Austin", "TX", "73301");

        when(profileRepository.findById(3)).thenReturn(Optional.of(existing));
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        Profile actual = profileService.update(3, update);

        // assert
        assertEquals(3, actual.getUserId(), "Update should keep the logged-in user's id");
        assertEquals("Jane", actual.getFirstName(), "First name should update");
        assertEquals("Doe", actual.getLastName(), "Last name should update");
        assertEquals("800-555-1111", actual.getPhone(), "Phone should update");
        assertEquals("jane.doe@email.com", actual.getEmail(), "Email should update");
        assertEquals("987 Cedar Street", actual.getAddress(), "Address should update");
        assertEquals("Austin", actual.getCity(), "City should update");
        assertEquals("TX", actual.getState(), "State should update");
        assertEquals("73301", actual.getZip(), "Zip should update");
    }

    @Test
    public void update_withMissingProfile_shouldReturnNull()
    {
        // arrange
        Profile update = createProfile(3, "George", "Jetson");

        when(profileRepository.findById(3)).thenReturn(Optional.empty());

        // act
        Profile actual = profileService.update(3, update);

        // assert
        assertNull(actual, "Missing profile should return null");
    }

    private Profile createProfile(int userId, String firstName, String lastName)
    {
        return new Profile(userId, firstName, lastName, "800-555-9876",
                "george.jetson@email.com", "123 Birch Parkway", "Dallas", "TX", "75051");
    }
}

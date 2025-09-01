package tech.kood.match_me.matching.dto;

import java.util.List;

public class MatchResponseDto {
    private final CurrentUserDto currentUser;
    private final List<MatchResultsDto> matches;

    public MatchResponseDto(CurrentUserDto currentUser, List<MatchResultsDto> matches) {
        this.currentUser = currentUser;
        this.matches = matches;
    }

    public CurrentUserDto getCurrentUser() { return currentUser; }
    public List<MatchResultsDto> getMatches() { return matches; }
}

package com.example.suleimanovfaiz_uur_sp;
public class PlayerScore {
    private final String nickname;
    private final int score;
    public PlayerScore(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }
    public String getNickname() {
        return nickname;
    }
    public int getScore() {
        return score;
    }
}

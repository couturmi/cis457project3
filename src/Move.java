/**
 * Created by mitchcout on 11/29/2017.
 */
public class Move {

    private int tileId;
    private boolean userWon;

    public Move(int tileId, boolean userWon) {
        this.tileId = tileId;
        this.userWon = userWon;
    }

    public int getTileId() {
        return tileId;
    }

    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    public boolean isUserWon() {
        return userWon;
    }

    public void setUserWon(boolean userWon) {
        this.userWon = userWon;
    }
}

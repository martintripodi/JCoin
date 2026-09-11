package martintripodi.JCoin;

import java.util.Objects;

public class MinableBlock {
    public String prevHash;
    public long timeStamp;
    public int difficulty;

    public MinableBlock() {

    }

    public MinableBlock(String prevHash, long timeStamp, int difficulty){
        this.prevHash = prevHash;
        this.timeStamp = timeStamp;
        this.difficulty = difficulty;
    }

    public boolean validateBlock(long nonce){
        nonce = Math.abs(nonce);
        String data = this.timeStamp + prevHash + Long.toString(nonce);
        String hash = Hash.calculateHash(data);
        if (hash.startsWith("0".repeat(this.difficulty))){
            return true;
        }
        return false;
    }
}

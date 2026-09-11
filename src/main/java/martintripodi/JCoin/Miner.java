package martintripodi.JCoin;

import java.security.SecureRandom;

public class Miner {
    private final SecureRandom random = new SecureRandom();
    MinableBlock prevMinableBlock;

    public long mine(MinableBlock block){
        if (block == null) return -1;
        String target = "0".repeat(block.difficulty);
        while (true){
            long nonce = random.nextLong(0,Long.MAX_VALUE);

            String data = block.timeStamp + block.prevHash + Long.toString(nonce);
            String hash = Hash.calculateHash(data);

            if (hash.startsWith(target)){
                System.out.println("Found nonce : " + Long.toString(nonce));
                return nonce;
            }
        }
    }
}

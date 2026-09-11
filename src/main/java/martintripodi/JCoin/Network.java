package martintripodi.JCoin;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Objects;
import java.io.*;
import java.net.*;

public class Network {

    private String cryptoSystem = "Ed25519";

    private ArrayList<Block> blocks = new ArrayList<>();
    private MinableBlock currentMinableBlock;
    private Wallet systemWallet;

    public Network(){
        this.generateMinableBlock();
        systemWallet = new Wallet();
        systemWallet.address = "system";
    }

    private boolean verifyTransaction(PublicKey publicKey, String transaction, byte[] signature) {
        try {
            Signature verifier = Signature.getInstance(cryptoSystem);
            verifier.initVerify(publicKey);
            verifier.update(transaction.getBytes(StandardCharsets.UTF_8));
            return verifier.verify(signature);
        } catch (Exception ex) {
            return false;
        }
    }

    private int blocksSentFromAddress(String address){ // also nonce
        // returns how many blocks an address produced
        int amount = 0;
        for (Block block : blocks) {
            if (Objects.equals(block.fromAddress, address)) {
                amount++;
            }
        }
        return amount;
    }

    private Block getLatestBlock(String address){
        Block latest = null;
        for (Block block : blocks) {
            if (Objects.equals(block.fromAddress, address)) {
                if (latest == null){
                    latest = block;
                    continue;
                }

                if (block.nonce > latest.nonce){
                    latest = block;
                }
            }
        }
        return latest;
    }

    private boolean correctNonce(String address, long targetNonce){
        int blocksSent = blocksSentFromAddress(address);
        return blocksSent == targetNonce;
    }

    private void generateMinableBlock(){
        long timeStamp = System.currentTimeMillis();
        String hash = "";
        if (!blocks.isEmpty()){
            hash = blocks.getLast().hash;
        }else{
            hash = "genesis";
        }
        MinableBlock minableBlock = new MinableBlock(hash,timeStamp,6);
        currentMinableBlock = minableBlock;
    }

    public MinableBlock getMinableBlock(){
        return currentMinableBlock;
    }

    public boolean verifyMinedBlock(long nonce,String address){
        if (currentMinableBlock.validateBlock(nonce)){
            Block reward = systemWallet.createBlock(address,100);
            blocks.add(reward);
            this.generateMinableBlock();
            return true;
        }
        return false;
    }

    public void pushBlock(Block block) {
        if (block.isBlockTampered()) return;
        if (!verifyTransaction(block.publicKey, block.getTransaction(), block.signature)) return;
        if (!(getWalletCoins(block.fromAddress, block.hash) >= block.amount)) return;
        if (!correctNonce(block.fromAddress,block.nonce)) return;
        Block latestBlock = getLatestBlock(block.fromAddress);
        if (latestBlock != null) {
            if (!Objects.equals(block.prevHash, getLatestBlock(block.fromAddress).hash)) return;
        }else{
            if (!Objects.equals(block.prevHash,"genesis")) return;
        }

        block.serverTimeStamp = System.currentTimeMillis();

        long difference = Math.abs(block.serverTimeStamp - block.timeStamp) / 1000; // 5 secs. discrepancy allowance
        if (difference > 5) return;

        blocks.add(block);
    }

    public int getWalletCoins(String address, String excludeHash) {
        int coins = 0;
        for (Block block : blocks) {
            if (Objects.equals(block.hash, excludeHash)) continue;

            boolean sent = Objects.equals(block.fromAddress, address);
            boolean received = Objects.equals(block.toAddress, address);

            if (sent && received) continue;

            if (received) {
                coins += block.amount;
            }

            if (sent) {
                // the wallet owner gave
                coins -= block.amount;
            }

        }
        return coins;
    }

    public int getWalletCoins(String address) {
        int coins = 0;
        for (Block block : blocks) {
            boolean sent = Objects.equals(block.fromAddress, address);
            boolean received = Objects.equals(block.toAddress, address);

            if (sent && received) continue;

            if (received) {
                coins += block.amount;
            }

            if (sent) {
                // the wallet owner gave
                coins -= block.amount;
            }

        }
        return coins;
    }
}

package martintripodi.JCoin;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.PublicKey;
import java.util.Objects;

public class Block {
    public String hash;
    public String prevHash;

    public String fromAddress;
    public String toAddress;

    public int amount;
    public long timeStamp;
    public long serverTimeStamp;

    public PublicKey publicKey;
    public byte[] signature;

    public long nonce;

    @JsonIgnore
    public String getTransaction(){
        return Long.toString(this.timeStamp) + this.prevHash + this.fromAddress + this.toAddress + Integer.toString(this.amount) + Long.toString(this.nonce);
    }

    public Block(){

    }

    public Block(long timeStamp,String fromAddress, String toAddress,String prevHash, int amount,PublicKey publicKey, byte[] signature,long nonce){
        this.timeStamp = timeStamp;
        this.prevHash = prevHash;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
        this.publicKey = publicKey;
        this.signature = signature;
        this.nonce = nonce;

        String inputs = getTransaction();
        this.hash = Hash.calculateHash(inputs);
    }

    @JsonIgnore
    public boolean isBlockTampered(){
        String inputs = getTransaction();
        String calculatedHash = Hash.calculateHash(inputs);
        return !Objects.equals(calculatedHash,this.hash);
    }
}

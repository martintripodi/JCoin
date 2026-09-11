package martintripodi.JCoin;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Objects;

public class Wallet {
    private String cryptoSystem = "Ed25519";

    public String address;
    private PrivateKey privateKey;
    public PublicKey publicKey;
    private long nonce;
    private String prevHash;

    public Wallet(){
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(cryptoSystem);
            KeyPair keyPair = generator.generateKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
            this.address = "JC" + Hash.calculateHash(publicKey.toString()); // JC as differentiator acronym
            this.nonce = 0;
            this.prevHash = "genesis";

        }catch(Exception ex){
            System.out.println("error creating wallet");
        }
    }

    public byte[] signTransaction(long timeStamp,String toAddress, int amount){
        try {
            Signature signer = Signature.getInstance(cryptoSystem);
            signer.initSign(this.privateKey);
            String data = Long.toString(timeStamp) + this.prevHash + this.address + toAddress + Integer.toString(amount) + Long.toString(nonce);
            signer.update(data.getBytes(StandardCharsets.UTF_8));
            return signer.sign();
        }catch(Exception ex){
            System.out.println("error signing transaction");
            return null;
        }
    }

    public Block createBlock(String toAddress,int amount){
        long timeStamp = System.currentTimeMillis();
        byte[] signature = signTransaction(timeStamp,toAddress,amount);
        Block block = new Block(timeStamp,this.address,toAddress,this.prevHash,amount,this.publicKey,signature,nonce);
        nonce++;
        prevHash = block.hash;
        return block;
    }
}

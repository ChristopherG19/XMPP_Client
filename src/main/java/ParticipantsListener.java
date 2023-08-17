/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/

import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.ParticipantStatusListener;
import org.jivesoftware.smackx.muc.UserStatusListener;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.parts.Resourcepart;

public class ParticipantsListener implements ParticipantStatusListener, UserStatusListener{
    @Override
    public void joined(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Participant joined: " + occupant);
    }

    @Override
    public void left(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Participant left: " + occupant);
    }

    @Override
    public void kicked(EntityFullJid participant, Jid actor, String reason) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Participant kicked: " + occupant + ", Reason: " + reason);
    }

    @Override
    public void voiceGranted(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Voice granted to participant: " + occupant);
    }

    @Override
    public void voiceRevoked(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Voice revoked from participant: " + occupant);
    }

    @Override
    public void banned(EntityFullJid participant, Jid actor, String reason) {
        String bannedParticipant = participant.getLocalpartOrNull().toString();
        String actorJid = actor.toString();
        System.out.println("Participant banned: " + bannedParticipant + ", Actor: " + actorJid + ", Reason: " + reason);
    }

    @Override
    public void membershipGranted(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Membership granted to participant: " + occupant);
    }

    @Override
    public void membershipRevoked(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Membership revoked from participant: " + occupant);
    }

    @Override
    public void moderatorGranted(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Moderator role granted to participant: " + occupant);
    }

    @Override
    public void moderatorRevoked(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Moderator role revoked from participant: " + occupant);
    }

    @Override
    public void ownershipGranted(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Ownership granted to participant: " + occupant);
    }

    @Override
    public void ownershipRevoked(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Ownership revoked from participant: " + occupant);
    }

    @Override
    public void adminGranted(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Admin role granted to participant: " + occupant);
    }

    @Override
    public void adminRevoked(EntityFullJid participant) {
        String occupant = participant.getLocalpartOrNull().toString();
        System.out.println("Admin role revoked from participant: " + occupant);
    }

    @Override
    public void nicknameChanged(EntityFullJid participant, Resourcepart newNickname) {
        String occupant = participant.getLocalpartOrNull().toString();
        String newNicknameStr = newNickname.toString();
        System.out.println("Nickname changed for participant: " + occupant + ", New nickname: " + newNicknameStr);
    }

    @Override
    public void kicked(Jid actor, String reason) {
        String actorJid = actor.toString();
        System.out.println("You were kicked from the room. Actor: " + actorJid + ", Reason: " + reason);
    }
    
    @Override
    public void voiceGranted() {
        System.out.println("Voice granted to you.");
    }
    
    @Override
    public void voiceRevoked() {
        System.out.println("Voice revoked from you.");
    }
    
    @Override
    public void banned(Jid actor, String reason) {
        String actorJid = actor.toString();
        System.out.println("You were banned from the room. Actor: " + actorJid + ", Reason: " + reason);
    }
    
    @Override
    public void membershipGranted() {
        System.out.println("Membership granted to you.");
    }
    
    @Override
    public void membershipRevoked() {
        System.out.println("Membership revoked from you.");
    }
    
    @Override
    public void moderatorGranted() {
        System.out.println("Moderator role granted to you.");
    }
    
    @Override
    public void moderatorRevoked() {
        System.out.println("Moderator role revoked from you.");
    }
    
    @Override
    public void ownershipGranted() {
        System.out.println("Ownership granted to you.");
    }
    
    @Override
    public void ownershipRevoked() {
        System.out.println("Ownership revoked from you.");
    }
    
    @Override
    public void adminGranted() {
        System.out.println("Admin role granted to you.");
    }
    
    @Override
    public void adminRevoked() {
        System.out.println("Admin role revoked from you.");
    }
    
    @Override
    public void roomDestroyed(MultiUserChat alternateMUC, String reason) {
        System.out.println("The room was destroyed. Reason: " + reason);
    }
}

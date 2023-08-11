package com.ashfakh.hermes.dto.WADTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WhatsAppRequestDTO {
    @JsonProperty("object")
    private String object;

    @JsonProperty("entry")
    private List<Entry> entries;

    public String getTo(){
        if(entries.get(0).getChanges().get(0).getValue().getContacts() != null)
            return entries.get(0).getChanges().get(0).getValue().getContacts().get(0).getWaId();
        else return "";
    }

    public String getFrom(){
        if(entries.get(0).getChanges().get(0).getValue().getMetadata() != null)
            return entries.get(0).getChanges().get(0).getValue().getMetadata().getDisplayPhoneNumber();
        else return "";
    }

    public String getName(){
        if(entries.get(0).getChanges().get(0).getValue().getContacts() != null)
            return entries.get(0).getChanges().get(0).getValue().getContacts().get(0).getProfile().getName();
        else return "";
    }


    public String getMessage(){
        if(entries.get(0).getChanges().get(0).getValue().getMessages() != null && entries.get(0).getChanges().get(0).getValue().getMessages().get(0).getText() != null)
            return entries.get(0).getChanges().get(0).getValue().getMessages().get(0).getText().getBody();
        else return "";
    }

    public String getId(){
        if(entries.get(0).getChanges().get(0).getValue().getMessages() != null && entries.get(0).getChanges().get(0).getValue().getMessages().get(0).getId() != null)
            return entries.get(0).getChanges().get(0).getValue().getMessages().get(0).getId();
        else return "";
    }

    public String getType(){
        if(entries.get(0).getChanges().get(0).getValue().getMessages() != null && entries.get(0).getChanges().get(0).getValue().getMessages().get(0).getType() != null)
            return entries.get(0).getChanges().get(0).getValue().getMessages().get(0).getType();
        else return "";
    }

    public String getInteractiveMessages(){
        if(entries.get(0).getChanges().get(0).getValue().getMessages() != null && entries.get(0).getChanges().get(0).getValue().getMessages().get(0).getInteractive() != null)
            return entries.get(0).getChanges().get(0).getValue().getMessages().get(0).getInteractive()
                    .getList_reply_str();
        else return "";
    }
}

@Data
class Entry {
    @JsonProperty("id")
    private String id;

    @JsonProperty("changes")
    private List<Change> changes;

}

@Data
class Change {
    @JsonProperty("value")
    private Value value;

    @JsonProperty("field")
    private String field;

    // getters and setters
}

@Data
class Value {
    @JsonProperty("messaging_product")
    private String messagingProduct;

    @JsonProperty("metadata")
    private Metadata metadata;

    @JsonProperty("contacts")
    private List<Contact> contacts;

    @JsonProperty("messages")
    private List<Message> messages;

}

@Data
class Metadata {
    @JsonProperty("display_phone_number")
    private String displayPhoneNumber;

    @JsonProperty("phone_number_id")
    private String phoneNumberId;

}

@Data
class Contact {
    @JsonProperty("profile")
    private Profile profile;

    @JsonProperty("wa_id")
    private String waId;

}

@Data
class Profile {
    @JsonProperty("name")
    private String name;

}

@Data
class Message {
    @JsonProperty("from")
    private String from;

    @JsonProperty("id")
    private String id;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("text")
    private Text text;

    @JsonProperty("type")
    private String type;

    @JsonProperty("interactive")
    private Interactive interactive;

}

@Data
class Text {
    @JsonProperty("body")
    private String body;
}

@Data
class Interactive {
    @JsonProperty("type")
    private String type;

    @JsonProperty("list_reply")
    private ListReply list_reply;

    public String getList_reply_str(){
        return list_reply.getTitle() + " " + list_reply.getDescription();
    }
}

@Data
class ListReply {
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;
}

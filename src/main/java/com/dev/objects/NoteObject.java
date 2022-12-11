package com.dev.objects;



import javax.persistence.*;



@Entity

@Table(name = "notes")

public class NoteObject {



    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column

    private int id;



    @Column

    private String text;



    @Column (name = "some_other_name")

    private String otherText;



    @ManyToOne

    @JoinColumn (name = "user_id")

    private UserObject userObject;



    public UserObject getUserObject() {

        return userObject;

    }



    public void setUserObject(UserObject userObject) {

        this.userObject = userObject;

    }



    public int getId() {

        return id;

    }



    public void setId(int id) {

        this.id = id;

    }



    public String getText() {

        return text;

    }



    public void setText(String text) {

        this.text = text;

    }



    public String getOtherText() {

        return otherText;

    }



    public void setOtherText(String otherText) {

        this.otherText = otherText;

    }

}
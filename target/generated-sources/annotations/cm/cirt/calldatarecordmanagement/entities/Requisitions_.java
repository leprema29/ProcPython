package cm.cirt.calldatarecordmanagement.entities;

import cm.cirt.calldatarecordmanagement.entities.Users;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-05-04T21:39:05")
@StaticMetamodel(Requisitions.class)
public class Requisitions_ { 

    public static volatile SingularAttribute<Requisitions, Date> dateCreation;
    public static volatile SingularAttribute<Requisitions, String> signature;
    public static volatile SingularAttribute<Requisitions, Date> dateModification;
    public static volatile SingularAttribute<Requisitions, Date> dateRequisition;
    public static volatile SingularAttribute<Requisitions, String> telephone;
    public static volatile SingularAttribute<Requisitions, Long> id;
    public static volatile SingularAttribute<Requisitions, Users> user;
    public static volatile SingularAttribute<Requisitions, Integer> version;
    public static volatile SingularAttribute<Requisitions, Integer> etat;

}
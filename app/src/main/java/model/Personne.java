package model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Sharaf on 16/11/2015.
 */
@Table(name="Personnne")
public class Personne extends Model {
    @Column(name="toto")
    public String toto;
}

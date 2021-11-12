/*
USUÁRIO APL ====================================================================
Esta classe abstrai para o sistema seus gestores e os enfermeiros. Um objeto 
dessa classe só pode ser um dos dois, sendo isso especificado pelo atributo tipo_
pessoa. Esta é a classe com mais permissões, isto é, com mais acesso às funciona-
lidades do projeto. Há, em pontos específicos do sistema, restrição de acesso a 
enfermeiros também, de modo que só gestores possam acessar. Essa é a classe dos 
usuários que gerenciam o projeto e os dados que por ele são guardados ou 
manipulados, daí o seu acesso a mais funções. Por exemplo, a pessoa responsável 
pelo registro de alguma vacinação, pelo controle do estoque de vacinas ou pela 
manutenção do cadastro de outros enfermeiros e gestores pertence a essa classe.
Seu SQL é:

create table usuario_apl(
    codigo serial primary key,
    cpf varchar(14) unique,
    nome varchar(40),
    tipo_pessoa varchar(1)
);
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Pedro
 */
@Entity
@Table(name = "usuario_apl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioApl.findAll", query = "SELECT u FROM UsuarioApl u"),
    @NamedQuery(name = "UsuarioApl.findByCodigo", query = "SELECT u FROM UsuarioApl u WHERE u.codigo = :codigo"),
    @NamedQuery(name = "UsuarioApl.findByCpf", query = "SELECT u FROM UsuarioApl u WHERE u.cpf = :cpf"),
    @NamedQuery(name = "UsuarioApl.findByNome", query = "SELECT u FROM UsuarioApl u WHERE u.nome = :nome"),
    @NamedQuery(name = "UsuarioApl.findByTipoPessoa", query = "SELECT u FROM UsuarioApl u WHERE u.tipoPessoa = :tipoPessoa")})
public class UsuarioApl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Size(max = 14)
    @Column(name = "cpf")
    private String cpf;
    @Size(max = 40)
    @Column(name = "nome")
    private String nome;
    @Size(max = 1)
    @Column(name = "tipo_pessoa")
    private String tipoPessoa;
    @OneToMany(mappedBy = "codigoUsuarioApl", fetch = FetchType.EAGER)
    private List<Vacinacao> vacinacaoList;

    public UsuarioApl() {
    }

    public UsuarioApl(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    @XmlTransient
    public List<Vacinacao> getVacinacaoList() {
        return vacinacaoList;
    }

    public void setVacinacaoList(List<Vacinacao> vacinacaoList) {
        this.vacinacaoList = vacinacaoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioApl)) {
            return false;
        }
        UsuarioApl other = (UsuarioApl) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.UsuarioApl[ codigo=" + codigo + " ]";
    }
    
}

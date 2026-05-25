package com.mycompany.testeoficial;

/*
 *
 * @author Joel
 */

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Testeoficial extends WindowAdapter implements ActionListener, Runnable {

    private Frame janela;
    private Panel painelEndereco, painelBotoes;
    private Label lNome, lVolta1, lVolta2, lTotal, lTempo;
    private TextField tNome, tVolta1, tVolta2, tTotal;
    private Button bNovo, bSalva, bExclui, bInicia, bConsulta;
    private Thread tempo;
    private int contador = 0;
    private boolean contando = false;
    private int controle = 1;

    public Testeoficial() {

        janela = new Frame();
        janela.setTitle("Agenda");
        janela.setSize(370,414);
        janela.setBackground(new Color(160,160,160));
        janela.setLayout(null);
        janela.addWindowListener(this);

        painelEndereco = new Panel();
        painelEndereco.setBackground(new Color(128,128,128));
        painelEndereco.setSize(350,234);
        painelEndereco.setLocation(10,80);
        painelEndereco.setLayout(null);

        painelBotoes = new Panel();
        painelBotoes.setBackground(new Color(64,128,128));
        painelBotoes.setSize(350,34);
        painelBotoes.setLocation(10,344);
        painelBotoes.setLayout(new FlowLayout());

        lNome = new Label("Nome:");
        lVolta1 = new Label("Volta 1:");
        lVolta2 = new Label("Volta 2:");
        lTotal = new Label("Total:");
        lTempo = new Label("Tempo: 0");

        tNome = new TextField(45);
        tVolta1 = new TextField(45);
        tVolta2 = new TextField(45);
        tTotal = new TextField(45);

        lNome.setBounds(10,20,60,20);
        lVolta1.setBounds(10,60,60,20);
        lVolta2.setBounds(10,100,60,20);
        lTotal.setBounds(10,140,60,20);
        lTempo.setBounds(120,180,100,20);

        tNome.setBounds(80,20,200,20);

        tVolta1.setBounds(80,60,200,20);
        tVolta2.setBounds(80,100,200,20);
        tTotal.setBounds(80,140,200,20);

        painelEndereco.add(lNome);
        painelEndereco.add(tNome);
        painelEndereco.add(lVolta1);
        painelEndereco.add(tVolta1);
        painelEndereco.add(lVolta2);
        painelEndereco.add(tVolta2);
        painelEndereco.add(lTotal);
        painelEndereco.add(tTotal);
        painelEndereco.add(lTempo);

        bNovo = new Button("Novo");
        bNovo.addActionListener(this);
        bSalva = new Button("Salva");
        bSalva.addActionListener(this);
        bExclui = new Button("Exclui");
        bExclui.addActionListener(this);
        bInicia = new Button("Inicia");
        bInicia.addActionListener(this);
        bConsulta = new Button("Consulta");
        bConsulta.addActionListener(this);

        painelBotoes.add(bNovo);
        painelBotoes.add(bSalva);
        painelBotoes.add(bExclui);
        painelBotoes.add(bInicia);
        painelBotoes.add(bConsulta);
        
        janela.add(painelEndereco);
        janela.add(painelBotoes);
    }

    public void setNome(String nome) {
        tNome.setText(nome);
    }

    public String getNome() {
        return tNome.getText();
    }

    public void setVolta1(String volta1) {
        tVolta1.setText(volta1);
    }

    public String getVolta1() {
        return tVolta1.getText();
    }

    public void setVolta2(String volta2) {
        tVolta2.setText(volta2);
    }

    public String getVolta2() {
        return tVolta2.getText();
    }

    public void setTotal(String total) {
        tTotal.setText(total);
    }

    public String getTotal() {
        return tTotal.getText();

    }

    
    public Connection conecta() {

        String url = "jdbc:mysql://localhost/telemetria";
        String usuario = "root";
        String senha = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, usuario, senha);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void actionPerformed(ActionEvent e) {

        Button b = (Button)e.getSource();

        if (b == bNovo)
            this.botaoNovo();

        else if (b == bSalva)
            this.botaoSalva();

        else if (b == bExclui)
            this.botaoExclui();

        else if (b == bInicia)
            this.botaoInicia();

        else if (b == bConsulta)
            this.botaoConsulta();
    }

    public void botaoNovo() {

        setNome("");
        setVolta1("");
        setVolta2("");
        setTotal("");
        contador = 0;
        controle = 1;
        contando = false;
        lTempo.setText("Tempo: 0");
    }

   
    public void botaoSalva() {

        try {

            Connection con = conecta();

            Statement st = con.createStatement();

            String sql = "insert into corrida(nome, volta1, volta2, total) values ('"
                    + getNome() + "', '"
                    + getVolta1() + "', '"
                    + getVolta2() + "', '"
                    + getTotal() + "')";

            st.executeUpdate(sql);

            System.out.println("Dados salvos!");

            con.close();

        } catch (Exception erro) {

            System.out.println(erro);

        }
    }

    
    public void botaoConsulta() {

        try {

            Connection con = conecta();
            Statement st = con.createStatement();
            String sql = "select * from corrida where nome='"
                    + getNome() + "'";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                setVolta1(rs.getString("volta1"));
                setVolta2(rs.getString("volta2"));
                setTotal(rs.getString("total"));
            }

            con.close();

            System.out.println("Consulta realizada!");

        } catch (Exception erro) {

            System.out.println(erro);
        }
    }

    
    public void botaoExclui() {

        try {

            Connection con = conecta();
            Statement st = con.createStatement();
            String sql = "delete from corrida where nome='"
                    + getNome() + "'";
            st.executeUpdate(sql);
            System.out.println("Dados excluidos!");
            botaoNovo();
            con.close();

        } catch (Exception erro) {
            System.out.println(erro);
        }
    }

  
    public void botaoInicia() {

        if(contando == false) {
            contando = true;
            tempo = new Thread(this);
            tempo.start();
            return;
        }

        if(controle == 1) {

            setVolta1(String.valueOf(contador));
            contador = 0;
            controle = 2;
            return;
        }

        if(controle == 2) {

            setVolta2(String.valueOf(contador));

            int v1 = Integer.parseInt(getVolta1());
            int v2 = Integer.parseInt(getVolta2());
            int total = v1 + v2;
            setTotal(String.valueOf(total));
            contando = false;
            controle = 1;
            contador = 0;
            lTempo.setText("Tempo: 0");
        }
    }

    
    public void run() {

        try {
            while(contando) {
                contador++;
                lTempo.setText("Tempo: " + contador);
                Thread.sleep(1000);
            }

        } catch(Exception erro) {
            System.out.println(erro);

        }
    }

  
    public void windowClosing(WindowEvent e) {

        System.exit(0);

    }

   
    public static void main(String[] args) {

        Testeoficial t = new Testeoficial();

        t.janela.setVisible(true);

    }
}
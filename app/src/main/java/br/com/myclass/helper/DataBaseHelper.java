package br.com.myclass.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gilmar on 31/07/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE = "Myclass";
    private static final int VERSAO = 34;

    public DataBaseHelper(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS turma;");
        db.execSQL("DROP TABLE IF EXISTS aluno;");
        db.execSQL("DROP TABLE IF EXISTS aula;");
        db.execSQL("DROP TABLE IF EXISTS atividade;");
        db.execSQL("DROP TABLE IF EXISTS alunoatividade;");
        db.execSQL("DROP TABLE IF EXISTS aulaprova;");
        db.execSQL("DROP TABLE IF EXISTS prova;");
        db.execSQL("DROP TABLE IF EXISTS nota;");
        db.execSQL("DROP TABLE IF EXISTS horario;");
        this.onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE turma(id INTEGER PRIMARY KEY,"
                + " disciplina TEXT NOT NULL,"
                + " curso TEXT NOT NULL,"
                + " datainicial TEXT NOT NULL,"
                + " datafinal TEXT NOT NULL,"
                + " descricao TEXT,"
                + " status TEXT);");

        db.execSQL("CREATE TABLE aluno(id INTEGER PRIMARY KEY,"
                + " foto TEXT,"
                + " galeria TEXT,"
                + " nome TEXT NOT NULL,"
                + " sobrenome TEXT NOT NULL,"
                + " endereco TEXT,"
                + " telefone TEXT,"
                + " email TEXT,"
                + " datanascimento TEXT,"
                + " descricao TEXT,"
                + " nomeresponsavel TEXT,"
                + " telefoneresponsavel TEXT,"
                + " emailresponsavel TEXT,"
                + " status TEXT,"
                + " turma_id INTEGER, "
                + " CONSTRAINT FK_turma FOREIGN KEY(turma_id) REFERENCES turma(id));");

        db.execSQL("CREATE TABLE aula(id INTEGER PRIMARY KEY,"
                + " conteudo TEXT NOT NULL,"
                + " data TEXT,"
                + " horainicio TEXT,"
                + " horafim TEXT,"
                + " descricao TEXT,"
                + " status TEXT,"
                + " turma_id INTEGER, "
                + " CONSTRAINT FK_turma FOREIGN KEY(turma_id) REFERENCES turma(id));");

        db.execSQL("CREATE TABLE atividade(id INTEGER PRIMARY KEY,"
                + " tipo TEXT NOT NULL,"
                + " descricao TEXT,"
                + " status TEXT,"
                + " aula_id INTEGER, "
                + " CONSTRAINT FK_aula FOREIGN KEY(aula_id) REFERENCES aula(id));");

        db.execSQL("CREATE TABLE alunoatividade(id INTEGER PRIMARY KEY,"
                + " status TEXT,"
                + " aluno_id INTEGER, "
                + " atividade_id INTEGER, "
                + " CONSTRAINT FK_aluno FOREIGN KEY(aluno_id) REFERENCES aluno(id),"
                + " CONSTRAINT FK_atividade FOREIGN KEY(atividade_id) REFERENCES atividade(id));");

        db.execSQL("CREATE TABLE prova(id INTEGER PRIMARY KEY,"
                + " data TEXT NOT NULL,"
                + " hora TEXT,"
                + " descricao TEXT,"
                + " status TEXT,"
                + " turma_id INTEGER, "
                + " CONSTRAINT FK_turma FOREIGN KEY(turma_id) REFERENCES turma(id));");

        db.execSQL("CREATE TABLE aulaprova(id INTEGER PRIMARY KEY,"
                + " aula_id TEXT,"
                + " prova_id TEXT,"
                + " status TEXT,"
                + " CONSTRAINT FK_aula FOREIGN KEY(aula_id) REFERENCES aula(id),"
                + " CONSTRAINT FK_prova FOREIGN KEY(prova_id) REFERENCES prova(id));");

        db.execSQL("CREATE TABLE nota(id INTEGER PRIMARY KEY,"
                + " nota TEXT,"
                + " pontoextra TEXT,"
                + " descricao TEXT,"
                + " status TEXT,"
                + " aluno_id TEXT,"
                + " prova_id TEXT,"
                + " CONSTRAINT FK_aluno FOREIGN KEY(aluno_id) REFERENCES aluno(id),"
                + " CONSTRAINT FK_prova FOREIGN KEY(prova_id) REFERENCES prova(id));");

        db.execSQL("CREATE TABLE horario(id INTEGER PRIMARY KEY,"
                + " dia TEXT NOT NULL,"
                + " horainicio TEXT,"
                + " horafim TEXT,"
                + " descricao TEXT,"
                + " status TEXT,"
                + " turma_id INTEGER, "
                + " CONSTRAINT FK_turma FOREIGN KEY(turma_id) REFERENCES turma(id));");


        // Insert Inicio

        // Inserindo Dados Tabela Turma
        //db.execSQL("INSERT INTO turma(id, descricao,status)VALUES(1, '3º ano de Informática','ativo')");// Id da turma 3º ano de Informática = 1, prof Jader
        // Inserindo Dados Tabela Aluno
        //db.execSQL("INSERT INTO aluno(id,foto,galeria,descricao,status,turma_id)VALUES(1,null,null,'Wemely Aparecida Bento','ativo',2)");// Id do Aluno Wemely = 1

        // Insert Fim
    }

    public static class Turma{
        public static final String TABELA = "turma";
        public static final String _ID = "id";
        public static final String DISCIPLINA = "disciplina";
        public static final String CURSO = "curso";
        public static final String DATAINICIAL = "datainicial";
        public static final String DATAFINAL = "datafinal";
        public static final String DESCRICAO = "descricao";
        public static final String STATUS = "status";

        public static final String[] COLUNAS = new String[]{_ID, DISCIPLINA, CURSO, DATAINICIAL, DATAFINAL, DESCRICAO, STATUS};

    }

    public static final class Aluno {
        public static final String TABELA = "aluno";
        public static final String _ID = "id";
        public static final String FOTO = "foto";
        public static final String GALERIA = "galeria";
        public static final String NOME = "nome";
        public static final String SOBRENOME = "sobrenome";
        public static final String ENDERECO = "endereco";
        public static final String TELEFONE = "telefone";
        public static final String EMAIL = "email";
        public static final String DATANASCIMENTO = "datanascimento";
        public static final String DESCRICAO = "descricao";
        public static final String NOMERESPONSAVEL = "nomeresponsavel";
        public static final String TELEFONERESPONSAVEL = "telefoneresponsavel";
        public static final String EMAILRESPONSAVEL = "emailresponsavel";
        public static final String STATUS = "status";
        public static final String TURMA_ID = "turma_id";

        public static final String[] COLUNAS = new String[]{_ID, FOTO, GALERIA, NOME,SOBRENOME, ENDERECO,TELEFONE, EMAIL, DATANASCIMENTO, DESCRICAO,NOMERESPONSAVEL,TELEFONERESPONSAVEL,EMAILRESPONSAVEL, STATUS, TURMA_ID};
    }

    public static final class Aula {
        public static final String TABELA = "aula";
        public static final String _ID = "id";
        public static final String CONTEUDO = "conteudo";
        public static final String DATA = "data";
        public static final String HORAINICIO = "horainicio";
        public static final String HORAFIM = "horafim";
        public static final String DESCRICAO = "descricao";
        public static final String STATUS = "status";
        public static final String TURMA_ID = "turma_id";

        public static final String[] COLUNAS = new String[]{_ID, CONTEUDO, DATA, HORAINICIO,HORAFIM, DESCRICAO, STATUS, TURMA_ID};

    }

    public static final class Atividade {
        public static final String TABELA = "atividade";
        public static final String _ID = "id";
        public static final String TIPO = "tipo";
        public static final String DESCRICAO = "descricao";
        public static final String STATUS = "status";
        public static final String AULA_ID = "aula_id";

        public static final String[] COLUNAS = new String[]{_ID, TIPO, DESCRICAO, STATUS, AULA_ID};

    }

    public static final class AlunoAtividade {
        public static final String TABELA = "alunoatividade";
        public static final String _ID = "id";
        public static final String STATUS = "status";
        public static final String ALUNO_ID = "aluno_id";
        public static final String ATIVIDADE_ID = "atividade_id";

        public static final String[] COLUNAS = new String[]{_ID, STATUS, ATIVIDADE_ID, ALUNO_ID};

    }

    public static final class Prova {
        public static final String TABELA = "prova";
        public static final String _ID = "id";
        public static final String DATA = "data";
        public static final String HORA = "hora";
        public static final String DESCRICAO = "descricao";
        public static final String STATUS = "status";
        public static final String TURMA_ID = "turma_id";

        public static final String[] COLUNAS = new String[]{_ID, DATA, HORA, DESCRICAO, STATUS, TURMA_ID};

    }

    public static final class AulaProva {
        public static final String TABELA = "aulaprova";
        public static final String _ID = "id";
        public static final String AULA_ID = "aula_id";
        public static final String PROVA_ID = "prova_id";
        public static final String STATUS = "status";

        public static final String[] COLUNAS = new String[]{_ID, AULA_ID, PROVA_ID, STATUS};
    }

    public static final class Nota {
        public static final String TABELA = "nota";
        public static final String _ID = "id";
        public static final String NOTA = "nota";
        public static final String PONTO_EXTRA = "pontoextra";
        public static final String DESCRICAO = "descricao";
        public static final String STATUS = "status";
        public static final String ALUNO_ID = "aluno_id";
        public static final String PROVA_ID = "prova_id";

        public static final String[] COLUNAS = new String[]{_ID, NOTA, PONTO_EXTRA, DESCRICAO, STATUS, ALUNO_ID, PROVA_ID};
    }

    public static final class Horario {
        public static final String TABELA = "horario";
        public static final String _ID = "id";
        public static final String DIA = "dia";
        public static final String HORAINICIO = "horainicio";
        public static final String HORAFIM = "horafim";
        public static final String DESCRICAO = "descricao";
        public static final String STATUS = "status";
        public static final String TURMA_ID = "turma_id";

        public static final String[] COLUNAS = new String[]{_ID, DIA, HORAINICIO,HORAFIM,  DESCRICAO, STATUS,TURMA_ID};

    }

}

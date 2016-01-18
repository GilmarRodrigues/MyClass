package br.com.myclass.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.myclass.helper.DataBaseHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.Turma;

/**
 * Created by gilmar on 01/08/15.
 */
public class AlunoDAO {
    private DataBaseHelper mHelper;
    private SQLiteDatabase db;

    public AlunoDAO(Context context) {
        mHelper = new DataBaseHelper(context);
    }

    private SQLiteDatabase getDb() {
        if (db == null) {
            db = mHelper.getWritableDatabase();
        }
        return db;
    }

    public void close() {
        mHelper.close();
        db = null;
    }

    public List<Aluno> listar(Turma turma) {
        String selection = DataBaseHelper.Aluno.TURMA_ID + " = ?";
        String[] selectionArgs = new String[]{turma.getId().toString()};

        Cursor cursor = getDb().query(DataBaseHelper.Aluno.TABELA,
                DataBaseHelper.Aluno.COLUNAS, selection, selectionArgs, null, null, null);
        List<Aluno> alunos = new ArrayList<Aluno>();
        while (cursor.moveToNext()) {
                Aluno aluno = criarAluno(cursor);
                if (aluno.getStatus().equals("ativo")) {
                    alunos.add(aluno);
                }
        }
        cursor.close();
        return alunos;
    }

    public List<Aluno> listarAtivos() {
        String selection = DataBaseHelper.Aluno.STATUS  + " = 'ativo'";

        Cursor cursor = getDb().query(DataBaseHelper.Aluno.TABELA,
                DataBaseHelper.Aluno.COLUNAS, selection, null, null, null, null);
        List<Aluno> alunos = new ArrayList<Aluno>();
        while (cursor.moveToNext()) {
            Aluno aluno = criarAluno(cursor);
            alunos.add(aluno);
        }
        cursor.close();
        return alunos;
    }

    public List<Aluno> listar() {
        Cursor cursor = getDb().query(DataBaseHelper.Aluno.TABELA,
                DataBaseHelper.Aluno.COLUNAS, null, null, null, null, null);
        List<Aluno> alunos = new ArrayList<Aluno>();
        while (cursor.moveToNext()) {
            Aluno aluno = criarAluno(cursor);
            alunos.add(aluno);
        }
        cursor.close();
        return alunos;
    }

    public Aluno buscarPorId(Long id) {
        Cursor cursor = getDb().query(DataBaseHelper.Aluno.TABELA,
                DataBaseHelper.Aluno.COLUNAS,
                DataBaseHelper.Aluno._ID + "=?",
                new String[]{id.toString()}, null, null, null);
        if (cursor.moveToNext()) {
            Aluno aluno = criarAluno(cursor);
            cursor.close();
            return aluno;
        }
        return null;
    }

    public long inserir(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Aluno.FOTO, aluno.getFoto());
        values.put(DataBaseHelper.Aluno.GALERIA, aluno.getGaleria());
        values.put(DataBaseHelper.Aluno.NOME, aluno.getNome());
        values.put(DataBaseHelper.Aluno.SOBRENOME, aluno.getSobrenome());
        values.put(DataBaseHelper.Aluno.ENDERECO, aluno.getEndereco());
        values.put(DataBaseHelper.Aluno.TELEFONE, aluno.getTelefone());
        values.put(DataBaseHelper.Aluno.EMAIL, aluno.getEmail());
        values.put(DataBaseHelper.Aluno.DATANASCIMENTO, aluno.getDataNascimento());
        values.put(DataBaseHelper.Aluno.DESCRICAO, aluno.getDescricao());
        values.put(DataBaseHelper.Aluno.NOMERESPONSAVEL, aluno.getNomeResponsavel());
        values.put(DataBaseHelper.Aluno.TELEFONERESPONSAVEL, aluno.getTelefoneResponsavel());
        values.put(DataBaseHelper.Aluno.EMAILRESPONSAVEL, aluno.getEmailResponsavel());
        values.put(DataBaseHelper.Aluno.STATUS, aluno.getStatus());
        values.put(DataBaseHelper.Aluno.TURMA_ID, aluno.getTurmaId());
        return getDb().insert(DataBaseHelper.Aluno.TABELA, null, values);
    }

    public long atualizar(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.Aluno.FOTO, aluno.getFoto());
        values.put(DataBaseHelper.Aluno.GALERIA, aluno.getGaleria());
        values.put(DataBaseHelper.Aluno.NOME, aluno.getNome());
        values.put(DataBaseHelper.Aluno.SOBRENOME, aluno.getSobrenome());
        values.put(DataBaseHelper.Aluno.ENDERECO, aluno.getEndereco());
        values.put(DataBaseHelper.Aluno.TELEFONE, aluno.getTelefone());
        values.put(DataBaseHelper.Aluno.EMAIL, aluno.getEmail());
        values.put(DataBaseHelper.Aluno.DATANASCIMENTO, aluno.getDataNascimento());
        values.put(DataBaseHelper.Aluno.DESCRICAO, aluno.getDescricao());
        values.put(DataBaseHelper.Aluno.NOMERESPONSAVEL, aluno.getNomeResponsavel());
        values.put(DataBaseHelper.Aluno.TELEFONERESPONSAVEL, aluno.getTelefoneResponsavel());
        values.put(DataBaseHelper.Aluno.EMAILRESPONSAVEL, aluno.getEmailResponsavel());
        values.put(DataBaseHelper.Aluno.STATUS, aluno.getStatus());
        values.put(DataBaseHelper.Aluno.TURMA_ID, aluno.getTurmaId());
        return getDb().update(DataBaseHelper.Aluno.TABELA, values, DataBaseHelper.Aluno._ID + "=?", new String[]{aluno.getId().toString()});
    }

    public boolean remover(Long id) {
        String whereClause = DataBaseHelper.Aluno._ID + "=?";
        String[] whereArgs = new String[]{id.toString()};
        int removidos = getDb().delete(DataBaseHelper.Aluno.TABELA,
                whereClause, whereArgs);
        return removidos > 0;
    }

    private Aluno criarAluno(Cursor cursor) {
        Aluno aluno = new Aluno(
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Aluno._ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.FOTO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.GALERIA)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.NOME)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.SOBRENOME)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.ENDERECO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.TELEFONE)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.EMAIL)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.DATANASCIMENTO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.DESCRICAO)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.NOMERESPONSAVEL)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.TELEFONERESPONSAVEL)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.EMAILRESPONSAVEL)),
                cursor.getLong(cursor.getColumnIndex(DataBaseHelper.Aluno.TURMA_ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseHelper.Aluno.STATUS))
        );
        return aluno;
    }

}

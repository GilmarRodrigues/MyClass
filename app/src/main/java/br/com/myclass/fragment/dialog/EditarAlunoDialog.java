package br.com.myclass.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.myclass.R;
import br.com.myclass.dao.AlunoDAO;
import br.com.myclass.helper.AlunoHelper;
import br.com.myclass.model.Aluno;
import br.com.myclass.model.ListAluno;
import br.com.myclass.utils.ImageUtils;
import br.com.myclass.utils.SDCardUtils;
import livroandroid.lib.view.RoundedImageView;

/**
 * Created by gilmar on 21/09/15.
 */
public class EditarAlunoDialog extends BaseDialog implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "Myclass";
    private Callback callback;
    private Aluno aluno;
    private EditText tDataNascimento;
    private File file;
    private RoundedImageView ivFoto;
    private int year, month, day;
    private AlunoHelper mHelper;

    public static void show(FragmentManager fm, Aluno aluno, Callback callback) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("adicionar_aluno");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        EditarAlunoDialog frag = new EditarAlunoDialog();
        frag.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(ListAluno.KEY, aluno);
        frag.setArguments(args);
        frag.show(ft, "adicionar_aluno");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Atualizar Aluno");
        View view = inflater.inflate(R.layout.dialog_aluno, container, false);
        Button btnAdcionar = (Button) view.findViewById(R.id.btn_editar);
        btnAdcionar.setOnClickListener(onClickAtualizar());
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(onClickCancelar());
        ivFoto = (RoundedImageView) view.findViewById(R.id.iv_foto);
        ivFoto.setOnClickListener(onClickFoto());
        mHelper = new AlunoHelper(getActivity(), view);
        tDataNascimento = (EditText) view.findViewById(R.id.edt_data_nascimento);
        tDataNascimento.setOnClickListener(onClickDataNascimento());
        tDataNascimento.setOnFocusChangeListener(onFocusChangeDataNascimento());

        this.aluno = (Aluno) getArguments().getSerializable(ListAluno.KEY);
        if (aluno != null) {
            mHelper.colocaAlunoNoFormulario(aluno);
            tDataNascimento.setText(aluno.getDataNascimento());

            if (aluno.getFoto() != null) {
                File file = new File(aluno.getFoto());
                ImageUtils.carregaImagemCamera(file, mHelper, ivFoto.getWidth(), ivFoto.getHeight(), ivFoto);
            } else if (aluno.getGaleria() != null) {
                try {
                    ImageUtils.carregaImagemGaleria(Uri.parse(aluno.getGaleria()), getActivity(), mHelper, ivFoto);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            transDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return view;
    }

    private View.OnClickListener onClickFoto() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibeDialog(getContext(), "Escolha uma opção.");
            }
        };
    }

    private View.OnClickListener onClickAtualizar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String novaNome = mHelper.tNome.getText().toString();
                String novaSobrenome = mHelper.tSobrenome.getText().toString();
                if (novaNome == null || novaNome.trim().length() == 0) {
                    mHelper.alert(getFragmentManager());
                    return;
                }
                if (novaSobrenome == null || novaSobrenome.trim().length() == 0) {
                    mHelper.alert(getFragmentManager());
                    return;
                }

                Context context = view.getContext();
                aluno = mHelper.pegaProdutoDoFormulario(novaNome, novaSobrenome);
                aluno.setTurmaId(aluno.getTurmaId());
                aluno.setStatus("ativo");
                aluno.setDataNascimento(tDataNascimento.getText().toString());
                AlunoDAO dao = new AlunoDAO(context);
                dao.atualizar(aluno);
                if (callback != null) {
                    callback.onAlunoAdd(aluno);
                }
                dismiss();
                dao.close();
            }
        };
    }

    private View.OnClickListener onClickCancelar() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                // ok para a foto
                ImageUtils.carregaImagemCamera(file, mHelper, ivFoto.getWidth(), ivFoto.getHeight(), ivFoto);
            } else {
                // cacenlar camera
                file = null;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    showImageGaleria(uri);
                }
            }
        }
    }

    private void showImageGaleria(Uri uri) {
        try {
            ImageUtils.carregaImagemGaleria(uri, getActivity(), mHelper, ivFoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exibeDialog(final Context context, final String mensagem) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_camera_galeria);
            dialog.setTitle(mensagem);
            final Button btnCamera = (Button) dialog.findViewById(R.id.btn_camera);
            final Button btnGaleria = (Button) dialog.findViewById(R.id.btn_galeria);
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Cria o caminho do arquivo no sdcard
                    file = SDCardUtils.getPrivateFile(getActivity().getBaseContext(), System.currentTimeMillis() + ".jpg",
                            Environment.DIRECTORY_PICTURES);
                    // Chama a intent informando o arquivo para salvar a foto
                    Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(irParaCamera, 123);
                    dialog.dismiss();
                }
            });
            btnGaleria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    /*if (Build.VERSION.SDK_INT < 19) {
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 2);
                    } else {
                        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, 2);
                    }*/
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private View.OnClickListener onClickDataNascimento() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        };
    }

    private View.OnFocusChangeListener onFocusChangeDataNascimento() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setData();
                }
            }
        };
    }

    public void setData() {
        initData();
        Calendar now = Calendar.getInstance();
        now.set(year,month,day);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                EditarAlunoDialog.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setOnCancelListener(EditarAlunoDialog.this);
        dpd.setAccentColor(Color.parseColor("#4CAF50"));
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private void  initData(){
        if(year == 0) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int yearAll, int monthOfYear, int dayOfMonth) {
        Calendar now = Calendar.getInstance();
        now.set(year,month,day);
        year = yearAll;
        month = monthOfYear;
        day = dayOfMonth;

        String data = day + "/" + (month + 1) + "/" + year;
        tDataNascimento.setText(data);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (year == 0) {
            year = month = day = 0;
            tDataNascimento.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    // Converte String para Data
    private void transDate() throws ParseException {
        String dataNull = tDataNascimento.getText().toString();
        if (dataNull == null || dataNull.trim().length() == 0) {
        } else {
            String data = aluno.getDataNascimento();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date d = formatter.parse(data);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH);
            year = c.get(Calendar.YEAR);
        }

    }

    // Interface para retornar o resultado
    public static interface Callback {
        public void onAlunoAdd(Aluno Aluno);
    }

}

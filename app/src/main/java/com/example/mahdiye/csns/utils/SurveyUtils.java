package com.example.mahdiye.csns.utils;

import android.database.Cursor;

import com.example.mahdiye.csns.SurveyActivityFragment;
import com.example.mahdiye.csns.models.survey.Survey;
import com.example.mahdiye.csns.models.survey.SurveyResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by Mahdiye on 6/22/2016.
 */
public class SurveyUtils {

    public static Survey getSurveyFromCursor(Cursor cursor) {
        byte[] blob = cursor.getBlob(SurveyActivityFragment.COL_SURVEY_JSON);

        ByteArrayInputStream bis = new ByteArrayInputStream(blob);
        ObjectInput in = null;
        Survey survey = null;
        try {
            in = new ObjectInputStream(bis);
            survey = (Survey) in.readObject();
        }catch(Exception e){
        }
        finally {
            try {
                bis.close();
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
            }
        }
        return  survey;

        /*String json = new String(blob);
        Gson gson = new Gson();

        return gson.fromJson(json, Survey.class);*/
    }

    public static byte[] getSurveyBytes(Survey survey) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(survey);
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                byteArrayOutputStream.close();
            } catch (Exception e) {
            }
        }
        return bytes;
    }

    public static byte[] getResponseBytes(SurveyResponse response) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(response);
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                byteArrayOutputStream.close();
            } catch (Exception e) {
            }
        }
        return bytes;
    }

    public static SurveyResponse getResponseFromBytes(byte[] bytes) {

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        SurveyResponse response = null;
        try {
            in = new ObjectInputStream(bis);
            response = (SurveyResponse) in.readObject();
        }catch(Exception e){
        }
        finally {
            try {
                bis.close();
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
            }
        }
        return  response;
    }

}

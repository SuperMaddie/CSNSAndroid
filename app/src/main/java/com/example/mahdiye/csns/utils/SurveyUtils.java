package com.example.mahdiye.csns.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.mahdiye.csns.MainActivity;
import com.example.mahdiye.csns.SurveyActivityFragment;
import com.example.mahdiye.csns.data.CSNSContract;
import com.example.mahdiye.csns.models.survey.Survey;
import com.example.mahdiye.csns.models.survey.SurveyResponse;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

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
    }

    public static SurveyResponse getSurveyResponseFromCursor(Cursor cursor) {
        int jsonColumnIndex = cursor.getColumnIndex(CSNSContract.SurveyResponseEntry.COLUMN_SURVEY_RESPONSE_JSON);
        byte[] blob = cursor.getBlob(jsonColumnIndex);

        ByteArrayInputStream bis = new ByteArrayInputStream(blob);
        ObjectInput in = null;
        SurveyResponse survey = null;
        try {
            in = new ObjectInputStream(bis);
            survey = (SurveyResponse) in.readObject();
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

    public static byte[] getSurveyResponseBytes(SurveyResponse response) {
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

    public static String getSurveyJson(Survey survey){
        Gson gson = new Gson();
        return gson.toJson(survey).toString();
    }

    public static Survey getSurveyFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Survey.class);
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

    public static String formatDate(long dateInMillis) {
        Date date = new Date(dateInMillis);
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);
        return DateFormat.getDateInstance().format(cal);
    }

    public static Calendar getCalendarFromMillis(long dateInMillis) {
        Date date = new Date(dateInMillis);
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static void sendBroadcast(Context context, String action){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        context.sendBroadcast(broadcastIntent);
    }

    public static void finishMainActivity(){
        MainActivity.mainActivity.finish();
    }

    public static void finishSurveyActivity(){
        SurveyActivityFragment.surveyActivity.finish();
    }
}

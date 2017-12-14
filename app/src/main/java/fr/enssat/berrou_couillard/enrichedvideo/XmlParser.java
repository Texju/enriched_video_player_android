package fr.enssat.berrou_couillard.enrichedvideo;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by berrou on 07/12/17.
 */

public class XmlParser {
    // We don't use namespaces
    private static final String ns = null;

    public static List <Movie> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private static List <Movie> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List <Movie> movies = new ArrayList();
        Movie movie;

        parser.require(XmlPullParser.START_TAG, ns, "resources");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("movie")) {
                movie = readMovie(parser);
                movies.add(movie);
            }
        }
        return movies;
    }

    private static Movie readMovie(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "movie");
        List <Chapter> chapters = new ArrayList();
        Chapter chapter;
        String url=null;
        String title=null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("chapter")) {
                chapter = readChapter(parser);
                chapters.add(chapter);
            } else if (name.equals("url")) {
                url = readUrl(parser);
            } else if (name.equals("title")) {
                title = readTitle(parser);
            } else {
                skip(parser);
            }
        }
        return new Movie(chapters, title, url);
    }

    private static Chapter readChapter(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "chapter");
        String title = null;
        String time = null;
        String url=null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("subtitle")) {
                title = readSubTitle(parser);
            } else if (name.equals("time")) {
                time = readTime(parser);
            } else if (name.equals("url")) {
                url = readUrl(parser);
            } else {
                skip(parser);
            }
        }
        return new Chapter(title, time, url);
    }

    private static String readSubTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "subtitle");
        String subtitle = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "subtitle");
        return subtitle;
    }

    private static String readUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "url");
        String url = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "url");
        return url;
    }

    // Processes title tags in the feed.
    private static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    // Processes title tags in the feed.
    private static String readTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "time");
        String time = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "time");
        return time;
    }

    // For the tags title and summary, extracts their text values.
    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}


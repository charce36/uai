using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Web;
using System.Xml;

namespace TFI.Envio.WebAPI.Helpers
{
    public class GMapsHelper
    {
        /// <summary>
        /// returns driving distance in miles
        /// </summary>
        /// <param name="origin"></param>
        /// <param name="destination"></param>
        /// <returns></returns>
        public static string GetDrivingDistanceInMiles(string origin, string destination)
        {
            var apikey = "AIzaSyCcWbImA7uZQ8oD9ov7onZcBOsBycw9KPw";

            string url = @"https://maps.googleapis.com/maps/api/distancematrix/xml?origins=" +
              origin + "&destinations=" + destination +
              "&mode=driving&language=en-EN&units=metric&key=" + apikey;

            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            WebResponse response = request.GetResponse();
            Stream dataStream = response.GetResponseStream();
            StreamReader sreader = new StreamReader(dataStream);
            string responsereader = sreader.ReadToEnd();
            response.Close();

            XmlDocument xmldoc = new XmlDocument();
            xmldoc.LoadXml(responsereader);

            if (xmldoc.GetElementsByTagName("status")[0].ChildNodes[0].InnerText == "OK")
            {
                XmlNodeList distance = xmldoc.GetElementsByTagName("distance");
                return distance[0].ChildNodes[1].InnerText;
            }

            return "0";
        }

        /// <summary>
        /// returns latitude 
        /// </summary>
        /// <param name="addresspoint"></param>
        /// <returns></returns>
        public double GetCoordinatesLat(string addresspoint)
        {
            using (var client = new WebClient())
            {
                string seachurl = "http://maps.google.com/maps/geo?q='" + addresspoint + "'&output=csv";
                string[] geocodeInfo = client.DownloadString(seachurl).Split(',');
                return (Convert.ToDouble(geocodeInfo[2]));
            }
        }

        /// <summary>
        /// returns longitude 
        /// </summary>
        /// <param name="addresspoint"></param>
        /// <returns></returns>
        public double GetCoordinatesLng(string addresspoint)
        {
            using (var client = new WebClient())
            {
                string seachurl = "http://maps.google.com/maps/geo?q='" + addresspoint + "'&output=csv";
                string[] geocodeInfo = client.DownloadString(seachurl).Split(',');
                return (Convert.ToDouble(geocodeInfo[3]));
            }
        }
    }
}
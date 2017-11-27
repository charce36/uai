using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Xml.Linq;
using TFI.Envio.Data;

namespace TFI.Envio.WebAPI.Controllers
{
    public class CostoReponse
    {
        public string Origen { get; set; }
        public string Destino { get; set; }
        public string Distancia { get; set; }
        public double Costo { get; set; }
    }
    public class CostoController : ApiController
    {
        private enviosEntities db = new enviosEntities();

        // GET: api/Costo/5
        public CostoReponse Get(int id)
        {
            var localidad = db.localidads.Where(X => X.codigopostal == id).FirstOrDefault();

            if (localidad == null)
            {
                return new CostoReponse()
                {
                    Origen = "ERROR",
                    Destino = "ERROR",
                    Distancia = "ERROR",
                    Costo = 0
                };
            }

            var from = "Pilar, Buenos Aires";
            var to = localidad.nombre + ", " + localidad.provincia.nombre; 
            var distance = Helpers.GMapsHelper.GetDrivingDistanceInMiles(from, to);

            NumberFormatInfo provider = new NumberFormatInfo();

            provider.NumberDecimalSeparator = ".";
            provider.NumberGroupSeparator = ",";
            provider.NumberGroupSizes = new int[] { 3 };


            var cost = Convert.ToDouble(distance.Replace(" km", ""), provider) * 5;

            return new CostoReponse() {
                Origen= from,
                Destino= to,
                Distancia = distance,
                Costo = cost
            };
        }
    }
}

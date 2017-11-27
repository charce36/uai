using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using TFI.Envio.Data;

namespace TFI.Envio.Services
{
    public class EnvioServices
    {
        private enviosEntities db = new enviosEntities();

        public localidad CalcularCosto(string codigoPostal)
        {
            var localidad = db.localidads.Where(x => x.codigopostal == Convert.ToInt16(codigoPostal)).SingleOrDefault();

            return localidad;
        }
    }
}

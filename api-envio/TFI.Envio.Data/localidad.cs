//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace TFI.Envio.Data
{
    using System;
    using System.Collections.Generic;
    
    public partial class localidad
    {
        public int id { get; set; }
        public byte provincia_id { get; set; }
        public string nombre { get; set; }
        public short codigopostal { get; set; }
    
        public virtual provincia provincia { get; set; }
    }
}

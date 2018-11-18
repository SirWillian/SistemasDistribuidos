using System;
using System.Collections.ObjectModel;
using System.ComponentModel.DataAnnotations;
using System.Runtime.Serialization;

namespace RazorPagesMovie.Models
{
    [DataContract(Namespace="")]
    public class Voo
    {
        [DataMember(Order=1)]
        [DataType(DataType.Date)]
        public DateTime data { get; set; }
        [DataMember(Order=2)]
        public string origem { get; set; }
        [DataMember(Order=3)]
        public string destino { get; set; }
        [DataMember(Order=4)]
        public string companhia { get; set; }
        [DataMember(Order=5)]
        public int preco { get; set; }
        [DataMember(Order=6)]
        public int assentosVagos { get; set; }
        

        public Voo(DateTime data, string origem, string destino, string companhia, int preco, int assentosVagos)
        {
            this.data=data;
            this.origem=origem;
            this.destino=destino;
            this.companhia=companhia;
            this.preco=preco;
            this.assentosVagos=assentosVagos;
        }
    }

    [CollectionDataContract(Name="vooes",Namespace="")]
    public class VooCollection : Collection<Voo>
    {
        
    }
}
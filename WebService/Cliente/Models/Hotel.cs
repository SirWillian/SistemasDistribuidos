using System;
using System.Collections.ObjectModel;
using System.Runtime.Serialization;

namespace RazorPagesMovie.Models
{
    [DataContract(Namespace="")]
    public class Hotel
    {
        [DataMember(Order=1)]
        public string nome { get; set; }
        [DataMember(Order=2)]
        public string local { get; set; }
        [DataMember(Order=3)]
        public int precoQuarto { get; set; }
        [DataMember(Order=4)]
        public int quartosVagos { get; set; }

        public Hotel(string nome, string local, int precoQuarto, int quartosVagos)
        {
            this.nome=nome;
            this.local=local;
            this.precoQuarto=precoQuarto;
            this.quartosVagos=quartosVagos;
        }
    }

    [CollectionDataContract(Name="hotels",Namespace="")]
    public class HotelCollection : Collection<Hotel>
    {
        
    }
}
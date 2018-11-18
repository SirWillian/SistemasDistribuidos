using System;
using System.Runtime.Serialization;

namespace RazorPagesMovie.Models
{
    [DataContract(Namespace="")]
    public class PassagemAerea
    {
        [DataMember(Order=0)]
        public bool idaVolta { get; set; }
        [DataMember(Order=1)]
        public string origem { get; set; }
        [DataMember(Order=2)]
        public string destino { get; set; }
        [DataMember(Order=3)]
        public string companhiaIda { get; set; }
        [DataMember(Order=4)]
        public string companhiaVolta { get; set; }
        [DataMember(Order=5)]
        public DateTime dataIda { get; set; }
        [DataMember(Order=6)]
        public DateTime dataVolta { get; set; }
        [DataMember(Order=7)]
        public int nPessoas { get; set; }

        public PassagemAerea(bool idaVolta, string origem, string destino, string companhiaIda, string companhiaVolta, DateTime dataIda, DateTime dataVolta, int nPessoas)
        {
            this.idaVolta=idaVolta;
            this.origem=origem;
            this.destino=destino;
            this.companhiaIda=companhiaIda;
            this.companhiaVolta=companhiaVolta;
            this.dataIda=dataIda;
            this.dataVolta=dataVolta;
            this.nPessoas=nPessoas;
        }
    }
}
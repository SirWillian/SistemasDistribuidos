using System;
using System.Runtime.Serialization;

namespace RazorPagesMovie.Models
{
    [DataContract(Namespace="")]
    public class Reserva
    {
        [DataMember(Order=1)]
        public string nome { get; set; }
        [DataMember(Order=2)]
        public string destino { get; set; }
        [DataMember(Order=3)]
        public int nQuartos { get; set; }
        [DataMember(Order=4)]
        public int nPessoas { get; set; }

        public Reserva(string nome, string destino, int nQuartos, int nPessoas)
        {
            this.nome=nome;
            this.destino=destino;
            this.nQuartos=nQuartos;
            this.nPessoas=nPessoas;
        }
    }
}
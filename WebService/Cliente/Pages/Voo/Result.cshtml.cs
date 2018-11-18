using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Http;
using RazorPagesMovie.Models;
using RazorPagesMovie;
using System.Net.Http.Formatting;
using System.Net.Http.Headers;
using Newtonsoft.Json;

namespace RazorPagesMovie.Pages
{
    public class VooResultModel : PageModel
    {
        public string Message { get; set; }
        public string VoosIdaHeader { get; set; }
        public string VoosVoltaHeader { get; set; }
        public VooCollection voosIda { get; set; }
        public VooCollection voosVolta { get; set; }

        public void OnGet()
        {
            Message = "wirue sarue";
        }
        /**  
         * Quando a página recebe um POST, faz o parsing da requisição (dos campos que podem não estar presentes/
         * não estiverem num formato adequado para conversão), guarda a PassagemAerea na sessão, conecta ao WebServer
         * e recebe as listas de voo de ida e volta(no caso de um voo de ida e volta) disponíveis para compra.
         */
        public async Task OnPostAsync()
        {
            Message = "Voos disponíveis";
            Console.WriteLine("I'm not here");
            HttpClient httpClient = new HttpClient();
            httpClient.DefaultRequestHeaders.Accept.Clear();
            httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));

            bool idaVolta=false;
            DateTime dataVolta;
            if(Request.Form["idaVolta"]=="on")
                idaVolta=true;
            if(Request.Form.ContainsKey("dataVolta"))
                dataVolta=DateTime.Parse(Request.Form["dataVolta"]);
            else
                dataVolta=DateTime.Parse("1990-01-01");
            PassagemAerea passagem = new PassagemAerea(idaVolta,Request.Form["origem"],Request.Form["destino"],"","",DateTime.Parse(Request.Form["dataIda"]),dataVolta,int.Parse(Request.Form["nPessoas"]));
            
            HttpContext.Session.SetString("passagem",JsonConvert.SerializeObject(passagem));

            string uri=Constants.serverPath+"/voo"+
                        "?origem="+passagem.origem+"&"+"&destino="+passagem.destino
                        +"&data="+passagem.dataIda.ToString()+"&nPessoas="+passagem.nPessoas;
            HttpResponseMessage response = await httpClient.GetAsync(uri);
            if(response.IsSuccessStatusCode)
                voosIda=await response.Content.ReadAsAsync<VooCollection>(new List<MediaTypeFormatter>{
                                                                new XmlMediaTypeFormatter(), 
                                                                new JsonMediaTypeFormatter()});
            
            if(idaVolta)
            {
                VoosIdaHeader="Voos de Ida";
                VoosVoltaHeader="Voos de Volta";
                uri=Constants.serverPath+"/voo"+
                        "?origem="+passagem.destino+"&"+"&destino="+passagem.origem
                        +"&data="+passagem.dataVolta+"&nPessoas="+passagem.nPessoas;
                
                response = await httpClient.GetAsync(uri);
                if(response.IsSuccessStatusCode)
                    voosVolta=await response.Content.ReadAsAsync<VooCollection>(new List<MediaTypeFormatter>{
                                                                new XmlMediaTypeFormatter(), 
                                                                new JsonMediaTypeFormatter()});
            }

            httpClient.Dispose();
        }
    }
}

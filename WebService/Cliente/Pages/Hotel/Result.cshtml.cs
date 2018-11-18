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
    public class HotelResultModel : PageModel
    {
        public string Message { get; set; }
        public HotelCollection hotels { get; set; }

        public void OnGet()
        {
            Message = "wirue sarue";
        }
        /**
         * Ao receber um POST, parseia a requisição, guarda a reserva desejada nos dados da sessão
         * e conecta ao WebServer, requisitando os hotéis referentes à reserva desejada.
         */
        public async Task OnPostAsync()
        {
            Message = "Hotéis disponíveis";
            Console.WriteLine("I'm not here");
            HttpClient httpClient = new HttpClient();
            httpClient.DefaultRequestHeaders.Accept.Clear();
            httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));
            Reserva reserva = new Reserva("",Request.Form["destino"],int.Parse(Request.Form["nQuartos"]),int.Parse(Request.Form["nPessoas"]));
            
            HttpContext.Session.SetString("reserva",JsonConvert.SerializeObject(reserva));

            string uri=Constants.serverPath+"/hotel"+
                        "?destino="+reserva.destino+"&nQuartos="+reserva.nQuartos.ToString()+
                        "&nPessoas="+reserva.nPessoas.ToString();
            HttpResponseMessage response = await httpClient.GetAsync(uri);
            if(response.IsSuccessStatusCode)
                hotels=await response.Content.ReadAsAsync<HotelCollection>(new List<MediaTypeFormatter>{
                                                                new XmlMediaTypeFormatter(), 
                                                                new JsonMediaTypeFormatter()});

            httpClient.Dispose();
        }
    }
}

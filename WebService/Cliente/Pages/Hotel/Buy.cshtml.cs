using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Newtonsoft.Json;
using RazorPagesMovie.Models;

namespace RazorPagesMovie.Pages
{
    public class HotelBuyModel : PageModel
    {
        public string Message { get; set; }
        public Reserva reserva { get ; set; }

        /**
         * Recupera a reserva desejada dos dados da sessão, preenche campos com dados do hotel e tenta fazer a reserva
         * acessando o WebServer.
         */
        public async Task OnPostAsync()
        {
            Message = "wirue sarue";
            Console.WriteLine("I'm not here");
            HttpClient httpClient = new HttpClient();
            httpClient.DefaultRequestHeaders.Accept.Clear();
            httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));
            httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("text/plain"));

            if(HttpContext.Session.Get("reserva")!=null)
            {
                reserva=JsonConvert.DeserializeObject<Reserva>(HttpContext.Session.GetString("reserva"));
                reserva.nome=Request.Form["nome"];
                reserva.destino=Request.Form["local"];

                HttpResponseMessage response = await httpClient.PostAsXmlAsync(Constants.serverPath+"/hotel",reserva);
                if(response.IsSuccessStatusCode)
                    Message=await response.Content.ReadAsStringAsync();
                else
                    Message="Ocorreu um erro.";
            }
            httpClient.Dispose();
        }
    }
}
